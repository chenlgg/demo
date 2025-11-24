package com.cl.demo1;

/**
 * @author: chenl
 * @since: 2025/9/18 16:25
 * @description:
 */
public class StarPatternDemo {
    public static void main(String[] args) {
        int size = 13; // 图案宽度和总高度
        int mid = size / 2; // 中间行索引 = 6

        for (int row = 0; row < size; row++) {
            StringBuilder line = new StringBuilder();

            if (row == 0 || row == mid || row == size - 1) {
                // 第0行、中间行、最后一行：全星
                for (int i = 0; i < size; i++) {
                    line.append('*');
                }
            } else {
                // 其他行：根据算法生成
                int upperRow = row < mid ? row : size - 1 - row; // 对称映射：下半部分映射到上半部分

                for (int col = 0; col < size; col++) {
                    if (col == 0 || col == size - 1) {
                        line.append('*'); // 左右边界
                    } else if (col == mid) {
                        line.append('*'); // 中间列
                    } else if (col == upperRow || col == size - 1 - upperRow) {
                        line.append('*'); // 收缩的斜线星号
                    } else {
                        line.append(' ');
                    }
                }
            }

            System.out.println(line.toString());
        }
    }

    public static void main1(String[] args) {
        int size = 13; // 图案宽度和总高度
        int mid = size / 2; // 中间行索引 = 6

        for (int row = 0; row < size; row++) {
            StringBuilder line = new StringBuilder();

            if (row == 0 || row == mid || row == size - 1) {
                // 第0行、中间行、最后一行：全星
                for (int i = 0; i < size; i++) {
                    line.append('*');
                }
            } else {
                // 其他行：根据算法生成
                int upperRow = row < mid ? row : size - 1 - row; // 对称映射：下半部分映射到上半部分
            }
        }
    }

    public static void main2(String[] args) {
        int size = 13; // 图案宽度和总高度
        int mid = size / 2; // 中间行索引 = 6

        for (int row = 0; row < size; row++) {
            StringBuilder line = new StringBuilder();

            if (row == 0 || row == mid || row == size - 1) {
                // 第0行、中间行、最后一行：全星
                for (int i = 0; i < size; i++) {
                    line.append('*');
                }
            } else {
                // 其他行：根据算法生成
                int upperRow = row < mid ? row : size - 1 - row; // 对称映射：下半部分映射到上半部分
            }
        }
    }

}
