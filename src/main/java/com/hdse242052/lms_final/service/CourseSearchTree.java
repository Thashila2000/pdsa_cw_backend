package com.hdse242052.lms_final.service;

import com.hdse242052.lms_final.entity.CourseEntity;
import java.util.*;

public class CourseSearchTree {
    private static class Node {
        String key;
        List<CourseEntity> courses = new ArrayList<>();
        Node left, right;

        Node(String key, CourseEntity course) {
            this.key = key;
            this.courses.add(course);
        }
    }

    private Node root;

    public void insert(String key, CourseEntity course) {
        root = insertRec(root, key.toLowerCase(), course);
    }

    private Node insertRec(Node node, String key, CourseEntity course) {
        if (node == null) return new Node(key, course);

        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = insertRec(node.left, key, course);
        else if (cmp > 0) node.right = insertRec(node.right, key, course);
        else node.courses.add(course);

        return node;
    }

    public List<CourseEntity> search(String query) {
        List<CourseEntity> result = new ArrayList<>();
        searchPartial(root, query.toLowerCase(), result);
        return result;
    }

    private void searchPartial(Node node, String query, List<CourseEntity> result) {
        if (node == null) return;

        if (node.key.contains(query)) {
            result.addAll(node.courses);
        }

        searchPartial(node.left, query, result);
        searchPartial(node.right, query, result);
    }
}