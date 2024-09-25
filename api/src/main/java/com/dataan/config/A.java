package com.dataan.config;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @author zhan bing liang
 * @date 2024/8/12 20:50
 */
public class A {
    static {

        System.out.println(111);
    }
    public static void radixSort(int[] arr) {
        // 获取数组中的最大数，确定需要排序的位数
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }

        // 获取最大数的位数
        int maxDigits = (max + "").length();

        // 用于存储每个桶中的数字
        int[][] buckets = new int[10][arr.length];

        // 用于记录每个桶中数字的实际数量
        int[] bucketCount = new int[10];

        // 进行maxDigits次排序
        for (int digit = 0; digit < maxDigits; digit++) {
            // 将原数组中的每个数分配到对应的桶中
            for (int i = 0; i < arr.length; i++) {
                int index = (arr[i] / (int) Math.pow(10, digit)) % 10;
                buckets[index][bucketCount[index]++] = arr[i];
            }

            // 收集桶中的数到原数组
            int pos = 0;
            for (int i = 0; i < bucketCount.length; i++) {
                if (bucketCount[i] != 0) {
                    for (int j = 0; j < bucketCount[i]; j++) {
                        arr[pos++] = buckets[i][j];
                    }
                    bucketCount[i] = 0; // 重置桶计数
                }
            }
        }
    }

    public static void main(String[] args) {
        String s = "abcdabd";
        int i = 0;
        int[] res = new int[s.length()];
        for (int j = 1; j < s.length(); j++) {
            if (s.charAt(j) == s.charAt(i)) {
                i++;
                res[j] = res[j - 1] + 1;
            } else {
                i = 0;
                res[j] = 0;
            }
        }
        System.out.println(Arrays.toString(res));
    }

}

class Node {
    int data;
    Node left, right;
    boolean leftThread, rightThread;

    Node(int item) {
        data = item;
        left = right = null;
        leftThread = rightThread = false;
    }

    public static void main(String[] args) {
        int arr[] = new int[]{1, 2, 3, 4, 5};
        int start = 0;
        int end = arr.length - 1;


        int res = find(arr, start, end, 5);

        System.out.println(res);
    }

    public static int find(int[] arr, int start, int end, int find) {
        if (start > end) {
            return -1;
        }
        int mid = (start + end) / 2;
        if (arr[mid] == find) {
            return mid;
        } else if (arr[mid] > find) {
            return find(arr, start, mid - 1, find);
        } else {
            return find(arr, mid + 1, end, find);
        }

    }
}
class sss{
    public static void main(String[] args) {
        int i = new sss().numDistinct("rabbbit", "rabbit");
        System.out.println(i);
    }
    public int numDistinct(String s, String t) {
        int[] dp=new int[t.length()+1];
        dp[0]=1;
        for (int i = 1; i <= s.length(); i++) {
            int pre=1;
            for (int j = 1; j <= i&&j<=t.length(); j++) {
                char c = s.charAt(i-1);
                char cc = t.charAt(j-1);
                int ts = pre;
                pre=dp[j];
                if (c == cc) {

                    dp[j]=ts+dp[j];
                } else {

                }

            }
        }
        return dp[dp.length-1];
        //return numDistinct(s,t,0,0,new HashMap<>());
    }

    private int numDistinct(String s, String t, int x, int y,Map<String,Integer> map) {
        int res =0;
        if (y == t.length()) {
            return 1;
        }
        if (x == s.length()) {
            return 0;
        }
        char ss = s.charAt(x);
        char yy = t.charAt(y);
        if (ss == yy) {
            res+=numDistinct(s,t,x+1,y+1,map)+numDistinct(s,t,x+1,y,map);
        } else {
            res+=numDistinct(s,t,x+1,y,map);
        }
        return res;
    }
}
class qi {
    private static int X;
    private static int Y;
    private static boolean visited[];
    private static boolean finished;

    public static void main(String[] args) {
        X = 8;
        Y = 8;
        int row = 1;
        int column = 1;
        int[][] chessboard = new int[X][Y];
        visited = new boolean[X * Y];
        long start = System.currentTimeMillis();
        traversalChessborad(chessboard, row - 1, column - 1, 1);
        long end = System.currentTimeMillis();
        System.out.println("共耗时:" + (end - start));

    }

    static List<Integer> list = new ArrayList<>();

    public static void traversalChessborad(int[][] chessboard, int row, int column, int step) {
//        if (list.contains(row * X + column)) {
//            return;
//        }
        chessboard[row][column] = step;
        visited[row * X + column] = true;
        ArrayList<Point> ps = next(new Point(column, row));
        //贪心优化算法
        sort(ps);
        while (!ps.isEmpty()) {
            Point p = ps.remove(0);
            if (!visited[p.y * X + p.x]) {
                traversalChessborad(chessboard, p.y, p.x, step + 1);
            }
        }
        //走到这里棋盘有两种情况:1.棋盘没有走完  2.棋盘走完了，处于回溯过程中
        if (step < X * Y && !finished) {
            chessboard[row][column] = 0;
            visited[row * X + column] = false;
            list.add(row * X + column);
        } else {
            finished = true;
        }
    }

    public static void sort(ArrayList<Point> ps) {
        ps.sort((o1, o2) -> {
            int size = next(o1).size();
            int size1 = next(o2).size();
            if (size < size1) {
                return -1;
            } else if (size == size1) {
                return 0;
            } else {
                return 1;
            }
        });
    }

    //计算马儿还能走哪些位置
    public static ArrayList<Point> next(Point curPoint) {
        ArrayList<Point> ps = new ArrayList<>();
        Point p1 = new Point();
        if ((p1.x = curPoint.x - 2) >= 0 && (p1.y = curPoint.y - 1) >= 0) {
            ps.add(new Point(p1));
        }
        if ((p1.x = curPoint.x - 2) >= 0 && (p1.y = curPoint.y + 1) < 8) {
            ps.add(new Point(p1));
        }
        if ((p1.x = curPoint.x - 1) >= 0 && (p1.y = curPoint.y - 2) >= 0) {
            ps.add(new Point(p1));
        }
        if ((p1.x = curPoint.x - 1) >= 0 && (p1.y = curPoint.y + 2) < 8) {
            ps.add(new Point(p1));
        }
        if ((p1.x = curPoint.x + 1) < 8 && (p1.y = curPoint.y + 2) < 8) {
            ps.add(new Point(p1));
        }
        if ((p1.x = curPoint.x + 1) < 8 && (p1.y = curPoint.y - 2) >= 0) {
            ps.add(new Point(p1));
        }
        if ((p1.x = curPoint.x + 2) < 8 && (p1.y = curPoint.y - 1) >= 0) {
            ps.add(new Point(p1));
        }
        if ((p1.x = curPoint.x + 2) < 8 && (p1.y = curPoint.y + 1) < 8) {
            ps.add(new Point(p1));
        }
        return ps;
    }
}

class Point {
    int x;
    int y;

    public Point() {
    }

    public Point(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class lianxi {
    public static void main(String[] args) {
        lianxi a = new lianxi();
        char[][] c = {{'x', 'x', 'x', 'x'},
            {'x', 'o', 'o', 'x'},
            {'x', 'x', 'o', 'x'},
            {'x', 'o', 'x', 'x'}};
        a.solve(c);
        Arrays.stream(c).forEach(s -> System.out.println(Arrays.toString(s)));

    }

    public void solve(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 'o') {
                    solve(board, i + 1, j + 1);
                }

            }
        }

    }

    public boolean solve(char[][] board, int i, int j, boolean[][] lu) {
        if (i == 0 || j == 0 || i == board.length + 1 || j == board[0].length + 1) {
            return false;
        }
        if (lu[i - 1][j - 1]) {
            return true;
        }
        lu[i - 1][j - 1] = true;

        if (board[i - 1][j - 1] == 'x') {
            return true;
        }

        boolean a1 = solve(board, i - 1, j, lu);
        boolean a2 = solve(board, i + 1, j, lu);
        boolean a3 = solve(board, i, j - 1, lu);
        boolean a4 = solve(board, i, j + 1, lu);
        boolean res = a1 && a2 && a3 && a4;
        if (res) {
            board[i - 1][j - 1] = 'x';
        }
        return res;
    }

    public void solve(char[][] board, int i, int j) {
        int s =i;
        int ss=j;
        boolean[][] lu = new boolean[board.length+2][board[0].length+2];
        Queue<Point> stack = new LinkedList<>();
        Point point = new Point(i, j);
        stack.offer(point);
        while (!stack.isEmpty()) {
            point = stack.poll();
            i = point.x;
            j = point.y;
            if (i == 0 || j == 0 || i == board.length + 1 || j == board[0].length + 1) {
                return;
            }

            lu[i][j] = true;
            if (board[i - 1][j - 1] == 'x') {
                continue;
            }
            if (!lu[i - 1][j]) {
                Point point1 = new Point(i-1, j);
                stack.offer(point1);

            }
            if (!lu[i+1][j]) {
                Point point2 = new Point(i+1, j);
                stack.offer(point2);

            }
            if (!lu[i][j - 1]) {
                Point point3 = new Point(i, j-1);
                stack.offer(point3);

            }
            if (!lu[i][j+1]) {
                Point point4 = new Point(i, j+1);
                stack.offer(point4);

            }

        }
        board[s - 1][ss - 1] = 'x';
    }
}
class cf{
    public static void main(String[] args) throws UnsupportedEncodingException, ClassNotFoundException, ExecutionException, InterruptedException {
        HashMap<Object, Object> m = new HashMap<>();
        m.put("2","2");
        m.put("1","1");
        m.entrySet().forEach(s-> System.out.println(s.getKey()));

    }
    public void test(){
        TreeMap treeMap = new TreeMap();

        List<String> list = new ArrayList();
        for(int i = 0; i<10000; i++){
            list.add("string" + i);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (list.size() > 0) {
                        String content = list.get(list.size() - 1);
                    }else {
                        break;
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if(list.size() <= 0){
                        break;
                    }
                    list.remove(0);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
class Foo {

    int x=1;
    public Foo() {

    }

    public static void main(String[] args) {
        Foo foo = new Foo();
        new Thread(()->{
            try {
                foo.second(()->{
                    System.out.println(2);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(()->{
            try {
                foo.third(()->{
                    System.out.println(3);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(()->{
            try {
                foo.first(()->{
                    System.out.println(1);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    public void first(Runnable printFirst) throws InterruptedException {
        synchronized (this) {
                printFirst.run();
                x = 2;
            this.notifyAll();
            // printFirst.run() outputs "first". Do not change or remove this line.
        }
    }

    public void second(Runnable printSecond) throws InterruptedException {
        synchronized (this) {
            while (x != 2) {
                this.wait();
            }
            printSecond.run();
            x = 3;
            this.notifyAll();
            // printFirst.run() outputs "first". Do not change or remove this line.
        }
    }

    public void third(Runnable printThird) throws InterruptedException {
        synchronized (this) {
            while (x != 3) {
                this.wait();
            }
            printThird.run();


            // printFirst.run() outputs "first". Do not change or remove this line.
        }
    }
}
