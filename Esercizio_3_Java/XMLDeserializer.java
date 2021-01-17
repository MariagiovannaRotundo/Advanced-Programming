package esercizio3;

import java.io.*;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Vector;

import javax.xml.parsers.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import esercizio2.XMLable;
import esercizio2.XMLfield;


public class XMLDeserializer {
	
	public XMLDeserializer(){}
	
	//take as input the name of the file and the packege where search 
	//the class. if pack="" search in the default package
	@SuppressWarnings({ "rawtypes", "unchecked", "resource" })
	public static Object[] deserialize(String fileName, String pack){
		
		if(pack==null){
			pack="";
		}
		
		//tag with the name of the class
		String className = "";
		//content of the file
		String filecontent;
		//class to deserialize
		Class c = null;
		//used in for cycles
		int i;
		//array to read the content of the file
		char[] arraybuffer = new char[1024];
		
		//to parse xml
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc = null;
		
		//if the name of file is not valid
		if(fileName.equals("")){
			System.out.println("Error: to specify name of file");
			return null;
		}
		
		//open the file
		File file = new File(fileName); 
		  
		//buffer to read the content of the file
		BufferedReader buffer = null;
		try {
			buffer = new BufferedReader(new FileReader(file));
			//read the first line (tag with class name)
			className = buffer.readLine();
			if(className!=null){
				String nameclass = pack +"."+ className.substring(1, className.length()-1);
				//read the class from his name
				c=Class.forName(nameclass);
			}
			else{
				//we don't have the name of the class: Error
				throw new ClassNotFoundException("class not found");
			}
		//if errors occur the program ends
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			try {
				buffer.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.exit(1);
		} 
		
		
		//check if the class is deserializable
		//if the class has not the annotation XMLabel
		if(!c.isAnnotationPresent(XMLable.class)){
			System.out.println("Error: class not deserializable");
			return null;
		}
		//check on constructors
		Constructor[] constructors = c.getDeclaredConstructors();
		for(i=0; i<constructors.length; i++){
			if(constructors[i].getParameterCount()==0)
				break;
		}
		//the class has not a constructor with no arguments
		if(i==constructors.length){
			System.out.println("Error: class not deserializable");
			return null;
		}
		
		//check on fields
		Field[] fields = c.getDeclaredFields();
		for(i=0;i<fields.length;i++){
			//if the field is not annotated
			if(!fields[i].isAnnotationPresent(XMLfield.class)){
				System.out.println("Error: class not deserializable");
				return null;
			}
			//if the field is static
			if(Modifier.isStatic(fields[i].getModifiers())){
				System.out.println("Error: class not deserializable");
				return null;
			}
			//if the fields is neither of primitive type nor String
			if(!fields[i].getType().isPrimitive() &&
					!fields[i].getType().getSimpleName().equals("String")){
				System.out.println("Error: class not deserializable");
				return null;
			}
			
		}
		
		//the class it's deserializable
		
		//read all content of the file and add an initial and ending 
		//tag to parse xml
		filecontent = "<XMLlist>\n" + className + "\n";
		try { 
			while((i = buffer.read(arraybuffer)) != -1){
				filecontent = filecontent + String.valueOf( Arrays.copyOfRange(arraybuffer, 0, i));
			}
			filecontent = filecontent + "\n</XMLlist>";
		//if an error happen the program ends
		} catch (IOException e) {
			e.printStackTrace();
			try {
				buffer.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.exit(1);
		}
		//close the buffer to read the file
		try {
			buffer.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//create builder to parse the xml
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(new InputSource(new StringReader(filecontent)));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (SAXException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		doc.getDocumentElement().normalize();
		
		//get the list of nodes of the class to deserialize 
		NodeList nodeList = doc.getElementsByTagName(className.substring(1, className.length()-1));
		//vector of objects of the class to deserialize
		Vector<Object> v = new Vector<>();
		//deserialization of each node
		for (int temp = 0; temp < nodeList.getLength(); temp++) {
			//nodes Student
			Node node = nodeList.item(temp);
			Object newObj = null;
			
			//creation of the object using the costructor with 0 args
			try {
				newObj = c.getConstructor().newInstance();
				v.add(newObj);
				
				//fields
				Element eElement = (Element) node;
				//for each field of the class to deserialize
				for(i=0;i<fields.length;i++){
					
					//get the annotation
					XMLfield ann = (XMLfield) fields[i].getAnnotation(XMLfield.class);
					//if the field has the annotation
					if(ann != null){
						String name;
						String value = null;
						//get the name using to save the field on the file during
						//serialization
						if(!ann.name().equals("")){
							name = ann.name();
						}
						else{
							name = fields[i].getName();
						}
					
						//get the value of the field saved on the file
						value = new String(eElement.getElementsByTagName(name).item(0).getTextContent());
						//assign the value to the field of the new object
						try {
							//assign the value to the field of the new object directly
							if(fields[i].isAccessible()){
								fields[i].set(newObj, toValue(fields[i].getType() ,value));
							}
							else{
								fields[i].setAccessible(true);
								fields[i].set(newObj, toValue(fields[i].getType() ,value));
							}
						} catch (SecurityException e) {
							Method[] methods = c.getMethods();
							for(int k=0;k<methods.length;k++){
								//try assign the value calling the set Method 
								//associated to the field if it exists
								if(methods[k].getName().startsWith("set")
									&& methods[k].getName().substring(3).toLowerCase().equals(name.toLowerCase())){
									methods[k].invoke(newObj, toValue(fields[i].getType() ,value));			
								}
							}
						} 	
					}
				}
				
				
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				System.exit(1);
			} catch (SecurityException e) {
				e.printStackTrace();
				System.exit(1);
			} catch (InstantiationException e) {
				e.printStackTrace();
				System.exit(1);
			}catch (InvocationTargetException e) {
				e.printStackTrace();
				System.exit(1);
			}catch (IllegalArgumentException e) {
				e.printStackTrace();
				System.exit(1);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		

		return v.toArray();
		  
	}
	
	@SuppressWarnings("rawtypes")
	//to convert a string in primitive types (or string)
	public static Object toValue(Class clazz, String value ) {
	    if( boolean.class == clazz ) return Boolean.parseBoolean( value );
	    if( byte.class == clazz ) return Byte.parseByte( value );
	    if( short.class == clazz ) return Short.parseShort( value );
	    if( long.class == clazz ) return Long.parseLong( value );
	    if( float.class == clazz ) return Float.parseFloat( value );
	    if( double.class == clazz ) return Double.parseDouble( value );
	    if( int.class == clazz ) return Integer.parseInt( value );
	    return value;
	}
	
	
}
