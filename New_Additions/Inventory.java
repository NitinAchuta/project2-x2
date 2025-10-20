package New_Additions;

/**
 * Inventory model class representing inventory items in the boba shop.
 * Based on the Inventory table schema.
 * 
 * @author harry
 * @version 1.0
 * @since 2024
 */
public class Inventory {
    private int ingredientID;
    private String ingredientName;
    private int ingredientCount;

    /**
     * Default constructor for Inventory.
     * 
     * @author harry
     */
    public Inventory() {
    }

    /**
     * Constructs a new Inventory item with specified parameters.
     * 
     * @param ingredientID the unique identifier for the ingredient
     * @param ingredientName the name of the ingredient
     * @param ingredientCount the current count/quantity of the ingredient
     * @author harry
     */
    public Inventory(int ingredientID, String ingredientName, int ingredientCount) {
        this.ingredientID = ingredientID;
        this.ingredientName = ingredientName;
        this.ingredientCount = ingredientCount;
    }

    /**
     * Gets the ingredient ID.
     * 
     * @return the ingredient ID
     * @author harry
     */
    public int getIngredientID() {
        return ingredientID;
    }

    /**
     * Sets the ingredient ID.
     * 
     * @param ingredientID the ingredient ID to set
     * @author harry
     */
    public void setIngredientID(int ingredientID) {
        this.ingredientID = ingredientID;
    }

    /**
     * Gets the ingredient name.
     * 
     * @return the ingredient name
     * @author harry
     */
    public String getIngredientName() {
        return ingredientName;
    }

    /**
     * Sets the ingredient name.
     * 
     * @param ingredientName the ingredient name to set
     * @author harry
     */
    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    /**
     * Gets the ingredient count.
     * 
     * @return the ingredient count
     * @author harry
     */
    public int getIngredientCount() {
        return ingredientCount;
    }

    /**
     * Sets the ingredient count.
     * 
     * @param ingredientCount the ingredient count to set
     * @author harry
     */
    public void setIngredientCount(int ingredientCount) {
        this.ingredientCount = ingredientCount;
    }

    /**
     * Returns a string representation of the Inventory item.
     * 
     * @return a formatted string containing ingredient name and count
     * @author harry
     */
    @Override
    public String toString() {
        return ingredientName + " (Count: " + ingredientCount + ")";
    }

    /**
     * Compares this Inventory item with another object for equality.
     * 
     * @param obj the object to compare with
     * @return true if the objects are equal, false otherwise
     * @author harry
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Inventory inventory = (Inventory) obj;
        return ingredientID == inventory.ingredientID;
    }

    /**
     * Returns a hash code for this Inventory item.
     * 
     * @return a hash code value for this object
     * @author harry
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(ingredientID);
    }
}