import time
import threading

#creation of the decorator. It takes 4 optional arguments
def benchmark(warmups = 0, iter = 1, verbose = False, csv_file = None):     
    #it takes a function os argument
    def real_benchmark(func):
        #the function can have parameters
        def repeat(*args, **kwargs):
            
            #list with times, one for warm-up invokation
            warmup_times = []
          
            #execution of the warm-up invokations whose number is given by warmups
            for i in range(warmups):
                start = time.time() 
                func(*args, **kwargs) 
                end = time.time() 
                #add to list the time between the end and the start of func (time of execution)
                warmup_times.append(end-start)
                
                #print information if required
                if verbose == True:
                    print("Time of the warm-up round number {}: {} s".format(i + 1, end-start))

            #list with times, one for invokation
            inv_times = []

            #execution of the warm-up invokations whose number is given by item
            for i in range(iter):
                start = time.time()
                func(*args, **kwargs) 
                end = time.time() 
                #append the time between the end and the start of func (time of execution)
                inv_times.append(end-start)

                #print information if required
                if verbose == True:
                    print("Time of the invocation number {}: {} s".format(i + 1, end-start))


            #average time of invocations (non warm-up)
            avg = sum(inv_times, 0) / len(inv_times)

            #calculation of the variance
            sum_element = 0
            for t in inv_times:
                sum_element += pow((t - avg),2)
            variance = sum_element / len(inv_times)

            #print of the results
            print("\n****** RESULTS ******")
            print("Warm-up iterations: {}".format(warmups))
            print("Number of invokations: {}".format(iter))
            print("avg time of execution: {} s".format(avg))
            print("variance: \n"+ str(variance))
            print("run num, is warmup, timing\n")
            for (i, item) in enumerate(warmup_times):
                print(str(i+1)+", true, "+ str(item))
            for (i, item) in enumerate(inv_times):
                print(str(i+warmups+1)+", false, "+ str(item))

            #save results in the file if required
            if csv_file is not None:
                f = open(csv_file+".csv", "w")
                #write the header
                f.write("run num, is warmup, timing\n")

                #write data
                for (i, item) in enumerate(warmup_times):
                    f.write(str(i+1)+", true, "+ str(item)+"\n")

                for (i, item) in enumerate(inv_times):
                    f.write(str(i+1)+", false, "+ str(item)+"\n")

                f.close()

        return repeat

    return real_benchmark


#function to test
def test(f, iterations, nthreads):
    
    #function to execute tests (with multiple thread)
    def run_test():

        #using the decorator with the function thread_task that define the 
        #task of a single thread
        @benchmark(0,1, True, f'./f_{nthreads}_{iterations}')
        def thread_task():
            for i in range(iterations):
                f()

        #array with threads
        threads = []
        for i in range(nthreads):
            threads.append(threading.Thread(target=thread_task, args=()))
            
        #start threads 
        for t in threads:
            t.start()

        #wait fot termination of the threads
        for t in threads:
            t.join()
    
    return run_test()
    


#function to calculate the nth number of Fibonacci (n = 32 by default)
def fibonacci(n=32): 
    if n<=0: 
        print("Incorrect number") 
        return 0
    elif n==1 or n==2: 
        return 1
    else: 
        return fibonacci(n-1)+fibonacci(n-2)


#main : execution of 4 tests
if __name__ == "__main__":
  print("\n********** TEST: 16 - 1 **********")  
  test(fibonacci, 16, 1)
  print("\n\n********** TEST: 8 - 2 **********")
  test(fibonacci, 8, 2)
  print("\n\n********** TEST: 4 - 4 **********")
  test(fibonacci, 4, 4)
  print("\n\n********** TEST: 2 - 8 **********")
  test(fibonacci, 2, 8)



"""
****** RESULTS ******:
--- first execution ---
16 - 1 : 13.205355167388916
8 - 2: 13.399935960769653
4 - 4: 17.792369604110718
2 - 8: 16.662595748901367

--- second execution ---
16 - 1 : 12.916890144348145
8 - 2: 13.401402473449707
4 - 4: 14.988358736038208
2 - 8: 17.758260011672974

"""