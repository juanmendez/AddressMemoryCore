package info.juanmendez.mapmemorycore.dependencies.db;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import io.realm.RealmModel;
import io.realm.RealmResults;

/**
 * Created by Juan Mendez on 7/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 * This is an adapter embedding realmResults to pass as a list.
 */

public class ResultsList<T extends RealmModel> implements List<T> {

    RealmResults<T> results;

    public ResultsList(RealmResults<T> results) {
        this.results = results;
    }

    public RealmResults<T> getResults() {
        return results;
    }

    @Override
    public int size() {
        return results.size();
    }

    @Override
    public boolean isEmpty() {
        return results.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return results.contains(o);
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return results.iterator();
    }

    @NonNull
    @Override
    public Object[] toArray() {

        RealmModel[] models = new RealmModel[results.size()];

        int len = results.size();

        for( int i = 0; i < len; i++ ){
            models[i] = results.get(i);
        }

        return models;
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] ts) {
        return null;
    }

    @Override
    public boolean add(T realmModel) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> collection) {
        return results.containsAll(collection);
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends T> collection) {
        return false;
    }

    @Override
    public boolean addAll(int i, @NonNull Collection<? extends T> collection) {
        return false;
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        return false;
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> collection) {
        return false;
    }

    @Override
    public void clear() {
    }

    @Override
    public T get(int i) {
        return results.get(i);
    }

    @Override
    public T set(int i, T realmModel) {
        return null;
    }

    @Override
    public void add(int i, T realmModel) {

    }

    @Override
    public T remove(int i) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return results.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return results.indexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return results.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator(int i) {
        return results.listIterator(i);
    }

    @NonNull
    @Override
    public List<T> subList(int i, int i1) {
        return results.subList(i, i1);
    }
}
