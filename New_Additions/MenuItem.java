package New_Additions;

/**
 * MenuItem model class representing a menu item in the boba shop
 * Based on the MenuItems table schema
 */
public class MenuItem {
    private int menuItemID;
    private String drinkCategory;
    private String menuItemName;
    private double price;

    // Constructors
    public MenuItem() {
    }

    public MenuItem(int menuItemID, String drinkCategory, String menuItemName, double price) {
        this.menuItemID = menuItemID;
        this.drinkCategory = drinkCategory;
        this.menuItemName = menuItemName;
        this.price = price;
    }

    // Getters and Setters
    public int getMenuItemID() {
        return menuItemID;
    }

    public void setMenuItemID(int menuItemID) {
        this.menuItemID = menuItemID;
    }

    public String getDrinkCategory() {
        return drinkCategory;
    }

    public void setDrinkCategory(String drinkCategory) {
        this.drinkCategory = drinkCategory;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return menuItemName + " - $" + String.format("%.2f", price);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        MenuItem menuItem = (MenuItem) obj;
        return menuItemID == menuItem.menuItemID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(menuItemID);
    }
}