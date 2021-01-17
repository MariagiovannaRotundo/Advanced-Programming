package esercizio2;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class XMLSerializer {

	public XMLSerializer(){}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void serialize(Object [ ] arr, String fileName){
		
		//if there are no object
		if(arr == null || arr.length == 0){
			System.out.println("Error: no objects to serialize");
			return;
		}
		
		//read which is the class to serialize
		Class c = arr[0].getClass();
		
		//check if the class is annotated
		if(c.isAnnotationPresent(XMLable.class)){
				
			//read the name of the class
			String nameClass = c.getSimpleName();
			
			//open file to write on it
			FileWriter myWriter = null;
			try {
				myWriter = new FileWriter(fileName + ".txt");
			} catch (IOException e2) {
				e2.printStackTrace();
				return;
			}
			
			//serialize each object
			for(int i=0; i<arr.length; i++){
				
				//string for serialize the single object
				String serializing="";
				serializing = serializing + "<" + nameClass + ">\n";
				
				Field[] fields = c.getDeclaredFields();
				
				for(int j=0; j<fields.length; j++){
					
					XMLfield ann = (XMLfield) fields[j].getAnnotation(XMLfield.class);
					
					//check that the field has the annotation
					if(ann != null){
						String name;
						Object value = null;
						//get the name to associate to the field in the serialization
						if(!ann.name().equals("")){
							name = ann.name();
						}
						else{
							name = fields[j].getName();
						}
						
						serializing = serializing +"\t<"+name+ " type=\""+ann.type()+"\">";
						//get the value of the field
						try {
							if(fields[j].isAccessible()){
								value = fields[j].get(arr[i]);
							}
							else{
								fields[j].setAccessible(true);
								value = fields[j].get(arr[i]);
							}
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
							break;
						} catch (IllegalAccessException e) {
							e.printStackTrace();
							break;
						}catch (SecurityException e) {
							
							Method[] methods = c.getMethods();
							for(int k=0;k<methods.length;k++){
								if(methods[k].getName().startsWith("get")
									&& methods[k].getName().substring(3).toLowerCase().equals(name.toLowerCase())){
									try {
										value = methods[k].invoke(arr[i]);
									} catch (IllegalAccessException e1) {
										e1.printStackTrace();
										break;
									} catch (IllegalArgumentException e1) {
										e1.printStackTrace();
										break;
									} catch (InvocationTargetException e1) {
										e1.printStackTrace();
										break;
									}			
								}
							}
						}
						
						serializing = serializing +value+"</"+ name+">\n";
					}
				}
				
				serializing = serializing + "</" + nameClass + ">\n";
				
				//write on file
				try {
					myWriter.write(serializing);
				}catch (IOException e) {
					System.out.println("An error occurred.");
				    e.printStackTrace();
				}		
			}
			//close the file
			try {
				myWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			System.out.println("Class not serializable");
		}
	}
}
