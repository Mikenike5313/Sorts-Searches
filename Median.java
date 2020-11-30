
public class Median {

	public static void selection_sort(long[] arr, int size) {
		for(int i = 0; i < size; ++i) {
			int pos = i;
			for(int j = i+1; j < size; ++j) {
				if(arr[j] < arr[pos]) {
					pos = j;
				}
			}
			long temp = arr[i];
			arr[i] = arr[pos];
			arr[pos] = temp;
		}
	}

	public static void insertion_sort(long[] arr, int size) {
		for(int i = 1; i < size; ++i) {
			long temp = arr[i];
			
			int j = i-1;
			while(j > -1 && arr[j] > temp) {
				arr[j+1] = arr[j--];
			}
			arr[j+1] = temp;
		}
	}

	private static void merge(long[] arr, int start, int mid, int end, long[] temp) {
		int l = start;
		int r = mid;
		int k = start;
		while(l < mid && r < end) {
			temp[k++] = (arr[l] < arr[r]) ? arr[l++] : arr[r++];
		}
		while(l < mid) {
			temp[k++] = arr[l++];
		}
		while(r < end) {
			temp[k++] = arr[r++];
		}
		for(int i = start; i < end; ++i) {
			arr[i] = temp[i];
		}
	}
	private static void recursive_merge_sort(long[] arr, int start, int end, long[] temp) {
		if(start < end-1) {
			int mid = (start+end)/2;
			recursive_merge_sort(arr, start, mid, temp);
			recursive_merge_sort(arr, mid, end, temp);
			merge(arr, start, mid, end, temp);
		}
	}
	public static void merge_sort(long[] arr, int start, int end) {
		long[] temp = new long[end-start];
		recursive_merge_sort(arr, start, end, temp);
	}

	private static int partition(long[] arr, int start, int end) {
		long atPivot = arr[end-1];
		int pivotIndex = start;
		for(int i = start; i < end-1; ++i) {
			if(arr[i] < atPivot) {
				if(i != pivotIndex) {
					long temp = arr[i];
					arr[i] = arr[pivotIndex];
					arr[pivotIndex] = temp;
				}
				pivotIndex++;
			}
		}
		if(end-1 != pivotIndex) {
			arr[end-1] = arr[pivotIndex];
			arr[pivotIndex] = atPivot;
		}
		return pivotIndex;
	}
	public static void quick_sort(long[] arr, int start, int end) {
		if(start < end) {
			int split = partition(arr, start, end);
			quick_sort(arr, start, split);
			quick_sort(arr, split+1, end);
		}
	}

	//non-negative integers only
	public static void radix_sort256(long[] arr, int size) {
		long[] output = new long[size];
		int radix = 256;
		int[] count = new int[radix]; //base 2^8 (1 byte)
		int reps = 8; //long is 8 bytes;
		for(int r = 0; r < reps; ++r) {
			//reset counts
			for(int i = 0; i < radix; ++i) {
				count[i] = 0;
			}
			//get counts
			for(int i = 0; i < size; ++i) {
				count[(int)((arr[i]>>r*8)&(long)255)]++;
			}
			//prefix sum
			for(int i = 1; i < 256; ++i) {
				count[i] += count[i-1];
			}
			//order
			for(int i = size-1; i > -1; --i) {
				output[--count[(int)((arr[i]>>r*8)&(long)255)]] = arr[i];
			}
			//swap refrences
			long[] temp = arr;
			arr = output;
			output = temp;
		}
		if(reps%2==1) { //if an odd number of swaps, swap final result back into arr
			long[] temp = arr;
			arr = output;
			output = temp;
		}
	}

	public static boolean is_non_decreasing(long[] arr, int size) {
		for(int i = 1; i < size; ++i) {
			if(arr[i-1] > arr[i]) {
				return false;
			}
		}
		return true;
	}

	//returns index of first occurance or -1
	public static int binary_search(long target, long[] arr, int size) {
		int start = 0;
		int end = size;
		while(start < end) {
			int mid = (start+end-1)/2;
			if(arr[mid] < target) {
				start = mid+1;
			}
			else if(arr[mid] > target) {
				end = mid;
			}
			else if(start != mid) {
				end = mid+1;
			}
			else {
				return mid;
			}
		}
		return -start-1;
	}


	public static void print(long[] arr, int size) {
		for(int i = 0; i < size; ++i) {
			System.out.print((i==0) ? "["+arr[i] : ", "+arr[i]);
		}
		System.out.println("] --- size = " + size);
	}


	public static void main(String[] args) {
		int size = 50;
		long[] arr = new long[size];

		//insert random values into array
		long lower = 0;
		long upper = 1000;
		for(int i = 0; i < size; ++i) {
			arr[i] = (long)Math.floor(Math.random()*(upper-lower)+lower);
		}

		System.out.println("Before Sort:");
		print(arr, size);

		//sort
		//selection_sort(arr, size);
		//insertion_sort(arr, size);
		//merge_sort(arr, 0, size);
		//quick_sort(arr, 0, size);
		radix_sort256(arr, size);

		System.out.println("After Sort:");
		print(arr, size);

		//verify sort
		System.out.println((is_non_decreasing(arr, size)) ? "Array was sorted correctly." : "Array was NOT sorted correctly.");

		//find median
		double median = (size%2==0) ? (arr[size/2-1]+arr[size/2])/2d : (double)arr[size/2];
		System.out.println("The median of these real numbers is " + median);

		//is median in the set?
		int index = binary_search((long)median, arr, size);
		System.out.println((Math.floor(median) == median && index < 0) ? "Median is at index " + index : "Median is not in the set");
	}
}
