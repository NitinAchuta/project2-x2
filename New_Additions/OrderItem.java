package New_Additions;

/**
 * OrderItem model class representing individual items within an order
 * Based on the OrderItems table schema
 */
public class OrderItem {
    private int orderItemID;
    private int orderID;
    private int menuItemID;
    private int quantity;

    // Customization options
    private int sugarLevel;
    private int iceLevel;
    private String milkType;

    // Toppings
    private int boba;
    private int lycheeJelly;
    private int grassJelly;
    private int pudding;
    private int aloeVera;
    private int redBean;
    private int coffeeJelly;
    private int coconutJelly;
    private int chiaSeeds;
    private int taroBalls;
    private int mangoStars;
    private int rainbowJelly;
    private int crystalBoba;
    private int cheeseFoam;
    private int whippedCream;
    private int oreoCrumbs;
    private int caramelDrizzle;
    private int matchaFoam;
    private int strawberryPoppingBoba;
    private int mangoPoppingBoba;
    private int blueberryPoppingBoba;
    private int passionfruitPoppingBoba;
    private int chocolateChips;
    private int peanutCrumble;
    private int marshmallows;
    private int cinnamonDust;
    private int honey;
    private int mintLeaves;

    // Constructors
    public OrderItem() {
    }

    public OrderItem(int orderItemID, int orderID, int menuItemID, int quantity) {
        this.orderItemID = orderItemID;
        this.orderID = orderID;
        this.menuItemID = menuItemID;
        this.quantity = quantity;
    }

    // Basic getters and setters
    public int getOrderItemID() {
        return orderItemID;
    }

    public void setOrderItemID(int orderItemID) {
        this.orderItemID = orderItemID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getMenuItemID() {
        return menuItemID;
    }

    public void setMenuItemID(int menuItemID) {
        this.menuItemID = menuItemID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Customization getters and setters
    public int getSugarLevel() {
        return sugarLevel;
    }

    public void setSugarLevel(int sugarLevel) {
        this.sugarLevel = sugarLevel;
    }

    public int getIceLevel() {
        return iceLevel;
    }

    public void setIceLevel(int iceLevel) {
        this.iceLevel = iceLevel;
    }

    public String getMilkType() {
        return milkType;
    }

    public void setMilkType(String milkType) {
        this.milkType = milkType;
    }

    // Topping getters and setters (simplified - including key ones)
    public int getBoba() {
        return boba;
    }

    public void setBoba(int boba) {
        this.boba = boba;
    }

    public int getLycheeJelly() {
        return lycheeJelly;
    }

    public void setLycheeJelly(int lycheeJelly) {
        this.lycheeJelly = lycheeJelly;
    }

    public int getGrassJelly() {
        return grassJelly;
    }

    public void setGrassJelly(int grassJelly) {
        this.grassJelly = grassJelly;
    }

    @Override
    public String toString() {
        return quantity + "x Item #" + menuItemID + " (Order #" + orderID + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        OrderItem orderItem = (OrderItem) obj;
        return orderItemID == orderItem.orderItemID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(orderItemID);
    }
}