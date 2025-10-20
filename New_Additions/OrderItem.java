package New_Additions;

/**
 * OrderItem model class representing individual items within an order.
 * 
 * This class serves as a data model for individual items within customer orders
 * in the POS system, corresponding to the OrderItems table in the database
 * schema.
 * It encapsulates all the essential information about an order item including
 * its
 * unique identifier, references to the parent order and menu item, quantity,
 * and
 * extensive customization options.
 * 
 * The class supports comprehensive drink customization including sugar levels,
 * ice levels, milk types, and a wide variety of toppings and add-ons. This
 * flexibility allows the boba shop to accommodate diverse customer preferences
 * and create personalized drink experiences.
 * 
 */
public class OrderItem {
    /** The unique identifier for the order item in the database */
    private int orderItemID;

    /** The ID of the parent order this item belongs to */
    private int orderID;

    /** The ID of the menu item being ordered */
    private int menuItemID;

    /** The quantity of this menu item ordered */
    private int quantity;

    // Customization options
    /**
     * Sugar level preference (0-100, where 0 is no sugar and 100 is maximum
     * sweetness)
     */
    private int sugarLevel;

    /** Ice level preference (0-100, where 0 is no ice and 100 is maximum ice) */
    private int iceLevel;

    /**
     * Type of milk used in the drink (e.g., "Regular", "Almond", "Oat", "Coconut")
     */
    private String milkType;

    // Toppings
    /** Quantity of regular boba pearls added to the drink */
    private int boba;

    /** Quantity of lychee jelly pieces added to the drink */
    private int lycheeJelly;

    /** Quantity of grass jelly pieces added to the drink */
    private int grassJelly;

    /** Quantity of pudding portions added to the drink */
    private int pudding;

    /** Quantity of aloe vera pieces added to the drink */
    private int aloeVera;

    /** Quantity of red bean portions added to the drink */
    private int redBean;

    /** Quantity of coffee jelly pieces added to the drink */
    private int coffeeJelly;

    /** Quantity of coconut jelly pieces added to the drink */
    private int coconutJelly;

    /** Quantity of chia seed portions added to the drink */
    private int chiaSeeds;

    /** Quantity of taro ball portions added to the drink */
    private int taroBalls;

    /** Quantity of mango star pieces added to the drink */
    private int mangoStars;

    /** Quantity of rainbow jelly pieces added to the drink */
    private int rainbowJelly;

    /** Quantity of crystal boba pearls added to the drink */
    private int crystalBoba;

    /** Quantity of cheese foam toppings added to the drink */
    private int cheeseFoam;

    /** Quantity of whipped cream portions added to the drink */
    private int whippedCream;

    /** Quantity of Oreo crumb portions added to the drink */
    private int oreoCrumbs;

    /** Quantity of caramel drizzle portions added to the drink */
    private int caramelDrizzle;

    /** Quantity of matcha foam toppings added to the drink */
    private int matchaFoam;

    /** Quantity of strawberry popping boba pearls added to the drink */
    private int strawberryPoppingBoba;

    /** Quantity of mango popping boba pearls added to the drink */
    private int mangoPoppingBoba;

    /** Quantity of blueberry popping boba pearls added to the drink */
    private int blueberryPoppingBoba;

    /** Quantity of passionfruit popping boba pearls added to the drink */
    private int passionfruitPoppingBoba;

    /** Quantity of chocolate chip portions added to the drink */
    private int chocolateChips;

    /** Quantity of peanut crumble portions added to the drink */
    private int peanutCrumble;

    /** Quantity of marshmallow portions added to the drink */
    private int marshmallows;

    /** Quantity of cinnamon dust sprinkles added to the drink */
    private int cinnamonDust;

    /** Quantity of honey drizzle portions added to the drink */
    private int honey;

    /** Quantity of mint leaf garnishes added to the drink */
    private int mintLeaves;

    // Constructors
    /**
     * Default constructor for OrderItem.
     * 
     * Creates a new OrderItem instance with default values. This constructor
     * is useful for frameworks that require a no-argument constructor or
     * when creating objects that will be populated later through setter methods.
     */
    public OrderItem() {
    }

    /**
     * Parameterized constructor for OrderItem.
     * 
     * Creates a new OrderItem instance with basic fields initialized to the
     * provided values. This constructor is useful for creating OrderItem objects
     * with core information, with customization options to be set separately.
     * 
     * @param orderItemID the unique identifier for the order item
     * @param orderID     the ID of the parent order this item belongs to
     * @param menuItemID  the ID of the menu item being ordered
     * @param quantity    the quantity of this menu item ordered
     */
    public OrderItem(int orderItemID, int orderID, int menuItemID, int quantity) {
        this.orderItemID = orderItemID;
        this.orderID = orderID;
        this.menuItemID = menuItemID;
        this.quantity = quantity;
    }

    // Basic getters and setters
    /**
     * Gets the order item ID.
     * 
     * @return the unique identifier for this order item
     */
    public int getOrderItemID() {
        return orderItemID;
    }

    /**
     * Sets the order item ID.
     * 
     * @param orderItemID the unique identifier to set for this order item
     */
    public void setOrderItemID(int orderItemID) {
        this.orderItemID = orderItemID;
    }

    /**
     * Gets the parent order ID.
     * 
     * @return the ID of the order this item belongs to
     */
    public int getOrderID() {
        return orderID;
    }

    /**
     * Sets the parent order ID.
     * 
     * @param orderID the ID of the order this item should belong to
     */
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    /**
     * Gets the menu item ID.
     * 
     * @return the ID of the menu item being ordered
     */
    public int getMenuItemID() {
        return menuItemID;
    }

    /**
     * Sets the menu item ID.
     * 
     * @param menuItemID the ID of the menu item being ordered
     */
    public void setMenuItemID(int menuItemID) {
        this.menuItemID = menuItemID;
    }

    /**
     * Gets the quantity ordered.
     * 
     * @return the number of this menu item ordered
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity ordered.
     * 
     * @param quantity the number of this menu item to order
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Customization getters and setters
    /**
     * Gets the sugar level preference.
     * 
     * @return the sugar level (0-100, where 0 is no sugar and 100 is maximum
     *         sweetness)
     */
    public int getSugarLevel() {
        return sugarLevel;
    }

    /**
     * Sets the sugar level preference.
     * 
     * @param sugarLevel the sugar level to set (0-100, where 0 is no sugar and 100
     *                   is maximum sweetness)
     */
    public void setSugarLevel(int sugarLevel) {
        this.sugarLevel = sugarLevel;
    }

    /**
     * Gets the ice level preference.
     * 
     * @return the ice level (0-100, where 0 is no ice and 100 is maximum ice)
     */
    public int getIceLevel() {
        return iceLevel;
    }

    /**
     * Sets the ice level preference.
     * 
     * @param iceLevel the ice level to set (0-100, where 0 is no ice and 100 is
     *                 maximum ice)
     */
    public void setIceLevel(int iceLevel) {
        this.iceLevel = iceLevel;
    }

    /**
     * Gets the milk type used in the drink.
     * 
     * @return the type of milk (e.g., "Regular", "Almond", "Oat", "Coconut")
     */
    public String getMilkType() {
        return milkType;
    }

    /**
     * Sets the milk type used in the drink.
     * 
     * @param milkType the type of milk to use (e.g., "Regular", "Almond", "Oat",
     *                 "Coconut")
     */
    public void setMilkType(String milkType) {
        this.milkType = milkType;
    }

    // Topping getters and setters (simplified - including key ones)
    /**
     * Gets the quantity of regular boba pearls.
     * 
     * @return the number of boba pearl portions added to the drink
     */
    public int getBoba() {
        return boba;
    }

    /**
     * Sets the quantity of regular boba pearls.
     * 
     * @param boba the number of boba pearl portions to add to the drink
     */
    public void setBoba(int boba) {
        this.boba = boba;
    }

    /**
     * Gets the quantity of lychee jelly pieces.
     * 
     * @return the number of lychee jelly portions added to the drink
     */
    public int getLycheeJelly() {
        return lycheeJelly;
    }

    /**
     * Sets the quantity of lychee jelly pieces.
     * 
     * @param lycheeJelly the number of lychee jelly portions to add to the drink
     */
    public void setLycheeJelly(int lycheeJelly) {
        this.lycheeJelly = lycheeJelly;
    }

    /**
     * Gets the quantity of grass jelly pieces.
     * 
     * @return the number of grass jelly portions added to the drink
     */
    public int getGrassJelly() {
        return grassJelly;
    }

    /**
     * Sets the quantity of grass jelly pieces.
     * 
     * @param grassJelly the number of grass jelly portions to add to the drink
     */
    public void setGrassJelly(int grassJelly) {
        this.grassJelly = grassJelly;
    }

    /**
     * Returns a string representation of this OrderItem.
     * 
     * The string format is "quantityx Item #menuItemID (Order #orderID)" which
     * provides a concise summary of the order item. This method is commonly
     * used for display purposes in user interfaces such as order summaries,
     * receipts, or debugging output.
     * 
     * @return a formatted string containing the quantity, menu item ID, and parent
     *         order ID
     */
    @Override
    public String toString() {
        return quantity + "x Item #" + menuItemID + " (Order #" + orderID + ")";
    }

    /**
     * Determines whether this OrderItem is equal to another object.
     * 
     * Two OrderItem objects are considered equal if they have the same
     * orderItemID. This follows the principle that the primary key uniquely
     * identifies each order item in the database, making it the appropriate
     * basis for equality comparison.
     * 
     * @param obj the object to compare with this OrderItem
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        OrderItem orderItem = (OrderItem) obj;
        return orderItemID == orderItem.orderItemID;
    }

    /**
     * Returns a hash code value for this OrderItem.
     * 
     * The hash code is based on the orderItemID, which is the unique identifier
     * for each order item. This ensures that equal OrderItem objects have the
     * same hash code, maintaining the contract between equals() and hashCode().
     * This is essential for proper behavior in hash-based collections like
     * HashMap and HashSet.
     * 
     * @return a hash code value for this OrderItem
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(orderItemID);
    }
}