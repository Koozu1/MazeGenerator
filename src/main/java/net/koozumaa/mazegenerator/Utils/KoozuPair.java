package net.koozumaa.mazegenerator.Utils;

public class KoozuPair<K, V> {

    private K key;

    public void setKey(K key) {
        this.key = key;
    }

    public void setValue(V value) {
        this.value = value;
    }

    private V value;

    public KoozuPair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey()	{ return key; }
    public V getValue() { return value; }
}
