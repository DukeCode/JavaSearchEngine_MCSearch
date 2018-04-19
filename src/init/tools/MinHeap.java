package init.tools;

import java.util.PriorityQueue;

public class MinHeap {
	private PriorityQueue<IdScorePair> minHeap;
	private int size;
	public MinHeap(int size) {
		this.size = size;
		minHeap = new PriorityQueue<>(size);
	}
	public IdScorePair poll() {
		return minHeap.poll();
	}
	public void offer(IdScorePair element) {
		if (minHeap.size() < size) {
			minHeap.offer(element);
		} else {
			minHeap.poll();
			minHeap.offer(element);
		}
	}
	public boolean isEmpty() {
		return minHeap.isEmpty();
	}
}
