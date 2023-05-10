package cola;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class COLAImpl<E extends Comparable<E>> implements Insert<E>, Query<E> {

    private final ArrayList<COLABlock<E>> data = new ArrayList<>();

    public void insertElement(E key) {
        COLABlock<E> insertBlock = new COLABlock<>(1);
        insertBlock.set(0, key);
        tryMerge(0, insertBlock);
    }

    private void tryMerge(int insertPosition, COLABlock<E> insertBlock) {
        COLABlock<E> mergeBlock = null;
        for (; insertPosition < data.size(); insertPosition++) {
            COLABlock<E> mergeCandidate = data.get(insertPosition);
            if (mergeCandidate.getSize() == insertBlock.getSize()) {
                mergeBlock = mergeCandidate;
                break;
            } else if (mergeCandidate.getSize() > insertBlock.getSize()) {
                break;
            }
        }
        if (mergeBlock != null) {
            mergeBlock.merge(insertBlock);
            data.remove(insertPosition);
            tryMerge(insertPosition, mergeBlock);
        } else {
            data.add(insertPosition, insertBlock);
        }
    }

    public E searchElement(E element) throws NoSuchElementException {
        int last = data.size() - 1;
        for (int i = 0; i <= last; i++) {
            COLABlock<E> current = data.get(i);
            E pair = current.search(element);
            if (pair != null)
                return pair;
        }
        throw new NoSuchElementException();
    }

}
