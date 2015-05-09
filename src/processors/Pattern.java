package processors;

public class Pattern {
    public int   size    = 0;
    public int[] pattern = null;
                    // ============== constructor ==============================
    public Pattern(int s){
        size = s;
        pattern = new int[size];
    }
}
