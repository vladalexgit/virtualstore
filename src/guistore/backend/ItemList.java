package guistore.backend;


import java.util.Collection;
import java.util.Comparator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public abstract class ItemList {

    private int size;
    private Node last;
    private Node first;

    private Comparator c;

    public ItemList(Comparator c) {
        last = null;
        first = null;
        size = 0;
        this.c = c;
    }

    private class Node<E>{

        private E value;
        private Node next,prev;

        private Node(E value, Node next, Node prev){
            this.value = value;
            this.next = next;
            this.prev = prev;
        }

        @Override
        public String toString() {
            return value.toString();
        }

    }

    private class ItemIterator implements ListIterator<Item>{

        private int cursor; // indexul urmatorului element ce va fi returnat (incepe de la 0)
        private Node crtNode; //incepe cu primul
        private Node returnedNode = null;

        private boolean flagNextCalled = false;

        private ItemIterator(){
            this.cursor = 0;
            this.crtNode = first;
        }

        private ItemIterator(int index){
            this.cursor = index;
            this.crtNode = ItemList.this.getNode(index);
        }

        @Override
        public boolean hasNext() {

            if(ItemList.this.isEmpty()){
                return false;
            }
            if(size == 1 && !flagNextCalled){
                return true;
            }else{
                return cursor < size-1;
            }

        }

        @Override
        public Item next() {
            if (hasNext()){

                if(!(crtNode == null && returnedNode!=null)){
                    returnedNode = crtNode;
                    crtNode = crtNode.next;

                    if(flagNextCalled) {
                        cursor++;
                    }
                    //altfel cursorul nu are nevoie de increment deoarece miscarea deabia incepe
                }else{
                    /*trateaza cazul cand am adaugat dupa ultimul element si acum crtNode este null dar avem totusi
                      un element urmator
                      (elementul urmator nu exista la momentul cand am folosit crtNode.next)*/
                    returnedNode = returnedNode.next;
                }

                flagNextCalled = true;

                return (Item)returnedNode.value;

            }else{
                throw new NoSuchElementException();
            }
        }

        @Override
        public boolean hasPrevious() {
            return cursor > 0;
        }

        @Override
        public Item previous() {

            if (hasPrevious()){

                if(returnedNode == null){
                    returnedNode = crtNode;
                    crtNode = crtNode.prev;
                    //cursorul nu are nevoie de increment deoarece miscarea deabia incepe
                }else {
                    returnedNode = returnedNode.prev;
                    crtNode = returnedNode;
                    cursor--;
                }

                return (Item)returnedNode.value;
            }else{
                throw new NoSuchElementException();
            }

        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void remove() {

            if ((returnedNode != null) && (ItemList.this.remove(ItemList.this.indexOf(returnedNode)))!=null) {

                //cand nodul este eliminat cursorul se muta pe elementul urmator (daca nu exista, pe cel anterior)
                //(daca lista este goala pe null)

                if (returnedNode.next != null) {
                   crtNode = returnedNode.next; //pt ca a fost eliminat

                } else if (returnedNode.prev != null) {
                    cursor--; //numai daca nu exista element urmator si l-am setat pe cel anterior in if
                    crtNode = returnedNode.prev;
                } else {
                    cursor--;
                    crtNode = null;
                }

                returnedNode = null;

            }else{
                throw new IllegalStateException();
            }
        }

        @Override
        public void set(Item item) {

            //pt a mentine lista sortata
            this.remove();
            this.add(item);

        }

        @Override
        public void add(Item item) {

            if(returnedNode == null){
                System.out.println("ERROR: next() nu a fost apelat!");
                return;
            }

            ItemList.this.add(item);

            if(c.compare(item,returnedNode.value)>0){
                return;
            }else { // daca elementul adaugat este mai mic decat cursorul inseamna ca a fost adaugat inainte
                cursor++; //cursorul creste numai daca am adaugat inaintea acestui element
            }

        }

    }


    @Override
    public String toString() {

        String buffer = "[";

        Node crtNode = first;
        if(first==null){
            buffer+="]";
        }
        while(crtNode!=null){
            if(crtNode.next == null){
                buffer+=crtNode.toString()+"]";
                break;
            }
            buffer+=crtNode.toString()+", ";
            crtNode = crtNode.next;

        }

        return buffer;

    }

    public boolean add(Item element){

        Node crtNode;

        if(last == null && first == null){

            crtNode = new Node(element, null, null);
            first = crtNode;
            last = crtNode;
            size++;
            return true;

        }else{

            crtNode = first;

            if(c.compare(element,crtNode.value)<0){
                crtNode.prev = new Node(element,first,null);
                first = crtNode.prev;
                size++;
                return true;
            }

            while(crtNode.next != null && c.compare(element,crtNode.next.value)>0){
                crtNode = crtNode.next;
            }

            crtNode.next = new Node(element,crtNode.next, crtNode);

            if(crtNode.next.next == null){
                last = crtNode.next;
            }else{
                //pentru a preveni cazul adaugarii a 2 elemente identice, cand next.prev ramane setat gresit
                crtNode.next.next.prev = crtNode.next;
            }

            size++;
            return true;

        }

    }

    public boolean addAll(Collection<? extends Item> c){

        for(Item i:c){
            add(i);
        }

        return true;

    }

    public Item getItem(int index){

        Node n = getNode(index);

        if(n==null){
            return null;
        }

        return (Item)n.value;

    }

    public Node<Item> getNode(int index){

        //returneaza -1 daca elementul nu a fost gasit

        if(index == -1 || first == null || index>=size) {
            System.out.println("index gresit sau lista vida");
            return null;
        }

        Node crtNode = first;

        for(int i=0;i<index;i++){
            crtNode = crtNode.next;
        }

        return crtNode;

    }

    public int indexOf(Item item){

        //returneaza -1 daca elementul nu a fost gasit

        if(first == null) {
            System.out.println("lista vida");
            return -1;
        }

        Node crtNode = first;
        int count = 0;

        while(crtNode != null){
            if(crtNode.value == item) {
                return count;
            }
            crtNode = crtNode.next;
            count++;
        }

        return -1;

    }

    public int indexOf(Node<Item> node){

        //returneaza -1 daca elementul nu a fost gasit

        if(first == null) {
            System.out.println("lista vida");
            return -1;
        }

        Node crtNode = first;
        int count = 0;

        while(crtNode != null){
            if(crtNode == node) {
                return count;
            }
            crtNode = crtNode.next;
            count++;
        }

        return -1;
    }

    public boolean contains(Node<Item> node){
        return indexOf(node)!=-1;
    }

    public boolean contains(Item item){
        return indexOf(item)!=-1;
    }

    public Item remove(int index){

        Node deleted = getNode(index);

        if(deleted == null){
            return null;
        }

        if(deleted.prev!=null){
            deleted.prev.next = deleted.next;
        }else{
            first = deleted.next;
        }
        if(deleted.next!=null){
            deleted.next.prev = deleted.prev;
        }else{
            last = deleted.prev;
        }

        size--;

        return (Item)deleted.value;
    }

    public boolean remove(Item item){

        Item deleted = remove(indexOf(item));
        if(deleted!=null){
            return true;
        }

        return false;
    }

    public boolean removeAll(Collection<? extends Item> collection){
        for(Item i:collection){
            remove(i);
        }
        return true;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public ListIterator<Item> listIterator(int index){
        return new ItemIterator(index);
    }

    public ListIterator<Item> listIterator(){
        return new ItemIterator();
    }

    public abstract Double getTotalPrice();

}
