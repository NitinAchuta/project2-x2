package New_Additions;

/**
 * MenuItem model class representing a menu item in the boba shop.
 * 
 * This class serves as a data model for menu items in the POS system,
 * corresponding to the MenuItems table in the database schema. It encapsulates
 * all the essential information about a menu item including its unique
 * identifier,
 * category, name, and price.
 * 
 * The class provides proper encapsulation through getter and setter methods,
 * implements standard Object methods for comparison and string representation,
 * and follows JavaBean conventions for easy integration with frameworks.
 * 
 */
public class MenuItem {
    /** The unique identifier for the menu item in the database */
    private int menuItemID;

    /** The category of the drink (e.g., "Tea", "Coffee", "Smoothie") */
    private String drinkCategory;

    /** The display name of the menu item */
    private String menuItemName;

    /** The price of the menu item in dollars */
    private double price;

    // Constructors
    /**
     * Default constructor for MenuItem.
     * 
     * Creates a new MenuItem instance with default values. This constructor
     * is useful for frameworks that require a no-argument constructor or
     * when creating objects that will be populated later.
     */
    public MenuItem() {
    }

    /**
     * Parameterized constructor for MenuItem.
     * 
     * Creates a new MenuItem instance with all fields initialized to the
     * provided values. This constructor is useful for creating fully
     * populated MenuItem objects from database records or user input.
     * 
     * @param menuItemID    the unique identifier for the menu item
     * @param drinkCategory the category of the drink
     * @param menuItemName  the display name of the menu item
     * @param price         the price of the menu item in dollars
     */
    public MenuItem(int menuItemID, String drinkCategory, String menuItemName, double price) {
        this.menuItemID = menuItemID;
        this.drinkCategory = drinkCategory;
        this.menuItemName = menuItemName;
        this.price = price;
    }

    // Getters and Setters
    /**
     * Gets the menu item ID.
     * 
     * @return the unique identifier for this menu item
     */
    public int getMenuItemID() {
        return menuItemID;
    }

    /**
     * Sets the menu item ID.
     * 
     * @param menuItemID the unique identifier to set for this menu item
     */
    public void setMenuItemID(int menuItemID) {
        this.menuItemID = menuItemID;
    }

    /**
     * Gets the drink category.
     * 
     * @return the category of this drink (e.g., "Tea", "Coffee", "Smoothie")
     */
    public String getDrinkCategory() {
        return drinkCategory;
    }

    /**
     * Sets the drink category.
     * 
     * @param drinkCategory the category to set for this drink
     */
    public void setDrinkCategory(String drinkCategory) {
        this.drinkCategory = drinkCategory;
    }

    /**
     * Gets the menu item name.
     * 
     * @return the display name of this menu item
     */
    public String getMenuItemName() {
        return menuItemName;
    }

    /**
     * Sets the menu item name.
     * 
     * @param menuItemName the display name to set for this menu item
     */
    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    /**
     * Gets the price of the menu item.
     * 
     * @return the price of this menu item in dollars
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the menu item.
     * 
     * @param price the price to set for this menu item in dollars
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Returns a string representation of this MenuItem.
     * 
     * The string format is "menuItemName - $price" with the price formatted
     * to two decimal places. This method is commonly used for display purposes
     * in user interfaces such as combo boxes or lists.
     * 
     * @return a formatted string containing the menu item name and price
     */
    @Override
    public String toString() {
        return menuItemName + " - $" + String.format("%.2f", price);
    }

    /**
     * Determines whether this MenuItem is equal to another object.
     * 
     * Two MenuItem objects are considered equal if they have the same
     * menuItemID. This follows the principle that the primary key uniquely
     * identifies each menu item in the database.
     * 
     * @param obj the object to compare with this MenuItem
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        MenuItem menuItem = (MenuItem) obj;
        return menuItemID == menuItem.menuItemID;
    }

    /**
     * Returns a hash code value for this MenuItem.
     * 
     * The hash code is based on the menuItemID, which is the unique identifier
     * for each menu item. This ensures that equal MenuItem objects have the
     * same hash code, maintaining the contract between equals() and hashCode().
     * 
     * @return a hash code value for this MenuItem
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(menuItemID);
    }
}