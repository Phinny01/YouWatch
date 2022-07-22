package com.example.youwatch;

import android.util.Log;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SearchRelevance {

    public static void getOccurrence(List<Post> objects, String toString, List<Post> postList) {
        int[] countArray = new int[objects.size()];
        Post[] posts = new Post[objects.size()];
        Post[] posts_2 = new Post[objects.size()];
        int i = 0;
        for (Post object : objects) {
            String caption = object.getDescription().toLowerCase(Locale.ROOT);
            int count = caption.length() - caption.replace(toString.toLowerCase(Locale.ROOT), "").length();
            countArray[i] = count;
            posts[i] = object;
            i++;
        }
        sort(countArray, 0, objects.size() - 1, posts);
        reverse(posts, objects, posts_2);
        postList.addAll(Arrays.asList(posts_2));
    }

    private static void merge(int[] arr, int left, int middle, int right, Post[] posts) {
        int sub_1 = middle - left + 1;
        int sub_2 = right - middle;
        int[] leftArray = new int[sub_1];
        int[] rightArray = new int[sub_2];
        Post[] leftPosts = new Post[sub_1];
        Post[] rightPosts = new Post[sub_2];
        for (int i = 0; i < sub_1; ++i) {
            leftPosts[i] = posts[left + i];
            leftArray[i] = arr[left + i];
        }
        for (int j = 0; j < sub_2; ++j) {
            rightArray[j] = arr[middle + 1 + j];
            rightPosts[j] = posts[middle + 1 + j];
        }
        int i = 0, j = 0;
        int k = left;
        while (i < sub_1 && j < sub_2) {
            if (leftArray[i] <= rightArray[j]) {
                arr[k] = leftArray[i];
                posts[k] = leftPosts[i];
                i++;
            } else {
                arr[k] = rightArray[j];
                posts[k] = rightPosts[j];
                j++;
            }
            k++;
        }
        while (i < sub_1) {
            arr[k] = leftArray[i];
            posts[k] = leftPosts[i];
            i++;
            k++;
        }
        while (j < sub_2) {
            arr[k] = rightArray[j];
            posts[k] = rightPosts[j];
            j++;
            k++;
        }
    }

    private static void sort(int[] arr, int left, int right, Post[] posts) {
        if (left < right) {
            int middle = left + (right - left) / 2;
            sort(arr, left, middle, posts);
            sort(arr, middle + 1, right, posts);
            merge(arr, left, middle, right, posts);
        }
    }

    private static void reverse(Post[] posts, List<Post> objects, Post[] posts_2) {
        int i = 0;
        for (int j = objects.size() - 1; j >= 0; j--) {
            posts_2[i] = (posts[j]);
            i++;
        }
    }

    public static List<String> getCaptionArray(List<Post> posts) {
        List<String> captions = Arrays.asList(new String[posts.size()]);
        int i = 0;
        for (Post post : posts) {
            captions.set(i, post.getDescription());
            i++;
        }
        return captions;
    }
}
