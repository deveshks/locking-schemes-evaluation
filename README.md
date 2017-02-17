The project contains implementation of a few fine-grained locking schemes operating on top of a sorted linked-list based set. 
A tiny benchmark to evaluate and compare the performance of different implementations is also implemented.  

We use the following implementation of the linked-list base set.
```
public interface IntSet {
  
    public boolean insert ( int x ) ;  // Return true if this set did not already contain the specified element.
    public boolean remove ( int x ) ; // Return true if this set contained the specified element.
    public boolean contain ( int x ) ; // Return true if this set contains the specified element.

}
```
The 3 locking schemes which are being evaluated extend this interface for their implementation as follows  
Coarse Grained Locking - SetList.java  
Hand-over hand Locking - HohList.java  
Optimistic with validation - OptimizedList.java  
To compile the code, run  
`javac *.java`

We then write a simple benchmark to evaluate and compare performance of different schemes, where we
spawn a bunch of threads, and compute the throughput of each scenario.The benchmark is configurable and has the following 
options  
-t Number of threads: e.g., 1, 2, 4, 6, 8  
-u Update ratios: what percentage, ranging from 0 to 100, of the operations are updates (i.e.,
insert()/remove()). E.g., 0, 10, 100. For simplicity, we just let there be half insert() and half remove()  
-i Initial list size: e.g., 100, 1k, 10k. It means how many elements are already in the set when
experiment begins. We also set the range of every element to be twice the size of initial-
list-size. This range also applies to function parameters invoked later on in the benchmark.
For example, if initially there are 100 elements, then each of these 100 elements is randomly
chosen from [0; 200), so is the parameters to invoke any function later on.  
-d Duration of the executing time, in the unit of milliseconds.  
-b Which scheme to test, could be "coarse", "hoh", or "optimistic".  

To run the code, use
`java Main -t <NumThreads> -u <UpdateRatio> -i <InitListSize> -d <duration> -b <scheme>`  

e.g. `java Main -t 8 -d 3000 -u 100 -i 10000 -b coarse`  
means to run the benchmark against Coarse-grained Locking implementation about 3 seconds of
8 threads, with all operations being insert() or remove(), initially the set has 10000 elements, all
values are randomly chosen from [0; 20000).  

One example of using this benchmark is to evaluate the for how many calls were success (returned true) for each method and how many calls were failed (returned false).
The statistics are then collected after running the benchmark against each implementation for the following
scenarios:  
1. Number of threads fixed to m is 8.  
2. Duration fixed to 5 seconds.  
3. Update ratio choseon from 10%; 100%.  
4. Initial list size chosen from 100; 1000; 10000.  

The observations are recorded in observations.pdf, and more information on the implementaiton and benchmark is present in hw.pdf




