public class IntList {
	int[] data;
	int length = 0;

	IntList(int i) {
		data = new int[i];
	}

	IntList() {
		data = new int[10];
	}

	void add(int elem) {
		if(length >= data.length)
			expand();

		data[length++] = elem;
	}

    void add(IntList elem) {
        for (int i = 0; i < elem.size(); i++)
            add(elem.get(i));
    }

	void clear() {
		//data = new int[length];
		length = 0;
	}

	boolean contains(int elem) {
		for (int i = 0; i < length; i++) {
			if (data[i] == elem) return true;
		}
		return false;
	}

	void expand() {
		int[] tmp = new int[length*2];

		for (int i = 0; i < length; i++) {
			tmp[i] = data[i];
		}

		data = tmp;
	}

	int get(int index) {
		if (index >= length || index < 0) return -1;
		return data[index];
	}

	int size() {
		return length;
	}
}