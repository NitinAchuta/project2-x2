package New_Additions;

/**
 * Inventory model class representing inventory items in the boba shop
 * Based on the Inventory table schema
 */
public class Inventory {
    private int ingredientID;
    private String ingredientName;
    private int ingredientCount;

    // Constructors
    public Inventory() {
    }

    public Inventory(int ingredientID, String ingredientName, int ingredientCount) {
        this.ingredientID = ingredientID;
        this.ingredientName = ingredientName;
        this.ingredientCount = ingredientCount;
    }

    // Getters and Setters
    public int getIngredientID() {
        return ingredientID;
    }

    public void setIngredientID(int ingredientID) {
        this.ingredientID = ingredientID;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public int getIngredientCount() {
        return ingredientCount;
    }

    public void setIngredientCount(int ingredientCount) {
        this.ingredientCount = ingredientCount;
    }

    @Override
    public String toString() {
        return ingredientName + " (Count: " + ingredientCount + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Inventory inventory = (Inventory) obj;
        return ingredientID == inventory.ingredientID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(ingredientID);
    }
}