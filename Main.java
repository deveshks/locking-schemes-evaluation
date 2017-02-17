import java.util.Random;
import java.util.concurrent.*;

public class Main {
    public static void main (String[] args) {
    	
    	if(args.length != 10){
		System.out.println("Please provide the arguments in the following format");
		System.out.println("java Main -t <NumThreads> -u <UpdateRatio> -i <InitListSize> -d <duration> -b <scheme>");
		return;
	}
        int totthreads=Integer.parseInt(args[1]);
        final int update_ratio = Integer.parseInt(args[3]);      
        final int listsize = Integer.parseInt(args[5]);
        int duration = Integer.parseInt(args[7]);
        String scheme = args[9];
        final IntSet list;
        if(scheme.equals("coarse")) {
            list = new SetList();
        }
        else if(scheme.equals("hoh"))
        {
            list = new HohList();
        }
        else
        {
            list = new OptimizedList();
        }
        Boolean present;
        int randomNum;
        Random rand = new Random();
        for(int i=0;i<listsize;i++)
        {
            present = true;
            while(present) {
                randomNum = rand.nextInt(2 * listsize);
                if (!list.contain(randomNum))
                {
                    list.insert(randomNum);
                    present = false;
                }
            }
        }

        ExecutorService executor = Executors.newFixedThreadPool(totthreads);

        final int[] total = {0};
        final int[] pass_insert = {0};
        final int[] pass_remove = {0};
        final int[] pass_contain = {0};
        final int[] fail_insert = {0};
        final int[] fail_remove = {0};
        final int[] fail_contain = {0};

        for(int i=0;i<totthreads;i++) {
            executor.execute(new Runnable() {
                public void run() {
                 while(!Thread.currentThread().isInterrupted());
                    Boolean pass;
                    int randomNum,randomOp;
                    for(int j=0;j<Integer.MAX_VALUE;j++) {
                    	randomNum = ThreadLocalRandom.current().nextInt(2 * listsize);
                    	randomOp = ThreadLocalRandom.current().nextInt(100);
                        if(randomOp < update_ratio) {
                            //update operation
                            if (randomOp < (update_ratio / 2)) {
                                pass = list.insert(randomNum);
                                if(pass)
                                    pass_insert[0]++;
                                else
                                    fail_insert[0]++;
                            }
                            else {
                                pass = list.remove(randomNum);
                                if(pass)
                                    pass_remove[0]++;
                                else
                                    fail_remove[0]++;
                            }
                        } else {
                            pass = list.contain(randomNum);
                            if(pass)
                                pass_contain[0]++;
                            else
                                fail_contain[0]++;
                        }
                        total[0]++;
                    }
                }
            });
        }

        executor.shutdown();

        try {
            // Wait a while for existing tasks to terminate
            if (!executor.awaitTermination(duration, TimeUnit.SECONDS)) {
                executor.shutdownNow(); // Cancel currently executing tasks
            }
	      if (!executor.awaitTermination(duration, TimeUnit.SECONDS))
	          { // System.err.println("Pool did not terminate");
		  }
        } catch (Exception e) {
            // (Re-)Cancel if current thread also interrupted
            executor.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Scheme: " + scheme);
        System.out.println("List Size: "+listsize);
	System.out.println("Update Ratio: "+update_ratio);
	System.out.println("Number of Threads: " + totthreads);
        System.out.println("Duration: "+duration + " seconds");
        System.out.println("Total Successful Calls: "+total[0]);
        System.out.println("Throughput: "+(total[0]/duration));
        System.out.println("Number of insert successes: "+pass_insert[0]);
        System.out.println("Number of insert failures: "+fail_insert[0]);
        System.out.println("Number of contains successes: "+pass_contain[0]);
        System.out.println("Number of contains failures: "+fail_contain[0]);
        System.out.println("Number of remove successes: "+pass_remove[0]);
        System.out.println("Number of remove failures: "+fail_remove[0]);
        System.exit(1);
    }
}
