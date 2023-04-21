package view;

public class MenuPrinter {
    public void print(String text, Colors color, int length) {
        int adjustLength = (length - 2 - text.length())%2;
        int gap = (int) (Math.floor(length - 2 - text.length()) / 2);
        System.out.print(" ");
        for (int i = 0; i < length - 2; i++)
            System.out.print("-");
        System.out.print(" ");
        System.out.print("\n|");
        System.out.print(color);
        for (int i = 0; i < length - 2; i++)
            System.out.print(" ");
        System.out.print(Colors.RESET);
        System.out.print("|\n|");
        System.out.print(color);
        for (int i = 0; i < gap; i++)
            System.out.print(" ");
        System.out.print(text);
        for (int i = 0; i < gap + adjustLength; i++)
            System.out.print(" ");
        System.out.print(Colors.RESET);
        System.out.print("|\n|");
        System.out.print(color);
        for (int i = 0; i < length - 2; i++)
            System.out.print(" ");
        System.out.print(Colors.RESET);
        System.out.print("|\n ");
        for (int i = 0; i < length - 2; i++)
            System.out.print("-");
        System.out.print(" ");
        System.out.println();
    }
}
