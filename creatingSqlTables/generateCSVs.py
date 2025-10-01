import random
import datetime
import csv

# Parameters
NUM_CUSTOMERS = 200
NUM_EMPLOYEES = 20
NUM_MENUITEMS = 20   # δ
WEEKS = 52           # α
ORDERS_PER_WEEK = 200
TOTAL_ORDERS = WEEKS * ORDERS_PER_WEEK

# Topping columns (must match your SQL schema for OrderItems)
toppings = [
    "boba", "lycheeJelly", "grassJelly", "pudding", "aloeVera", "redBean",
    "coffeeJelly", "coconutJelly", "chiaSeeds", "taroBalls", "mangoStars",
    "rainbowJelly", "crystalBoba", "cheeseFoam", "whippedCream", "oreoCrumbs",
    "caramelDrizzle", "matchaFoam", "strawberryPoppingBoba", "mangoPoppingBoba",
    "blueberryPoppingBoba", "passionfruitPoppingBoba", "chocolateChips",
    "peanutCrumble", "marshmallows", "cinnamonDust", "honey", "mintLeaves"
]

# Utility
def random_date(start, end):
    delta = end - start
    int_delta = (delta.days * 24 * 60 * 60) + delta.seconds
    random_second = random.randrange(int_delta)
    return start + datetime.timedelta(seconds=random_second)

# Date range
start_date = datetime.datetime.now() - datetime.timedelta(weeks=WEEKS)
end_date = datetime.datetime.now()

# Customers
with open("customers.csv", "w", newline="") as f:
    writer = csv.writer(f)
    writer.writerow(["customerID", "name", "phoneNum", "email"])
    for cid in range(1, NUM_CUSTOMERS + 1):
        writer.writerow([cid, f"Customer {cid}", f"555-01{cid:03d}", f"cust{cid}@email.com"])

# Employees
with open("employees.csv", "w", newline="") as f:
    writer = csv.writer(f)
    writer.writerow(["employeeID", "employeeName", "employeeRole", "hoursWorked"])
    for eid in range(1, NUM_EMPLOYEES + 1):
        role = random.choice(["Cashier", "Barista", "Manager"])
        writer.writerow([eid, f"Employee {eid}", role, random.randint(10, 40)])

# MenuItems
with open("menuitems.csv", "w", newline="") as f:
    writer = csv.writer(f)
    writer.writerow(["menuItemID", "drinkCategory", "menuItemName", "price"])
    categories = ["Milk Tea", "Fruit Tea", "Smoothie", "Coffee"]
    for mid in range(1, NUM_MENUITEMS + 1):
        category = random.choice(categories)
        price = round(random.uniform(3, 7), 2)
        writer.writerow([mid, category, f"Drink {mid}", price])

# Inventory
ingredients = ["Tea Base", "Milk", "Sugar", "Ice", "Boba", "Lychee Jelly",
               "Grass Jelly", "Pudding", "Straw", "Cup", "Lid", "Napkin",
               "To-go Box", "Bag", "Syrup", "Coffee", "Oat Milk", "Soy Milk",
               "Coconut Jelly", "Matcha"]
with open("inventory.csv", "w", newline="") as f:
    writer = csv.writer(f)
    writer.writerow(["ingredientID", "ingredientName", "ingredientCount"])
    for iid, name in enumerate(ingredients, start=1):
        writer.writerow([iid, name, random.randint(100, 500)])

# Orders, OrderItems, Payments
order_id = 1
orderitem_id = 1
payment_id = 1

with open("orders.csv", "w", newline="") as fo, \
     open("orderitems.csv", "w", newline="") as fi, \
     open("payments.csv", "w", newline="") as fp:
    
    orders_writer = csv.writer(fo)
    orderitems_writer = csv.writer(fi)
    payments_writer = csv.writer(fp)
    
    orders_writer.writerow(["orderID", "timeOfOrder", "customerID", "employeeID", "totalCost", "orderWeek"])
    orderitems_writer.writerow(
        ["orderItemID", "orderID", "menuItemID", "sugarLevel", "iceLevel", "milkType"] + toppings + ["quantity"]
    )
    payments_writer.writerow(["paymentID", "orderID", "paymentMethod", "status"])
    
    for _ in range(TOTAL_ORDERS):
        dt = random_date(start_date, end_date)
        cid = random.randint(1, NUM_CUSTOMERS)
        eid = random.randint(1, NUM_EMPLOYEES)
        menuitem = random.randint(1, NUM_MENUITEMS)
        qty = random.randint(1, 3)
        price = round(random.uniform(3, 7), 2)
        total = price * qty
        
        # Orders
        orders_writer.writerow([
            order_id, dt.strftime("%Y-%m-%d %H:%M:%S"), cid, eid, total, dt.isocalendar()[1]
        ])
        
        # OrderItems
        row = [
            orderitem_id, order_id, menuitem,
            random.choice([0,25,50,75,100]),  # sugarLevel
            random.choice([0,25,50,75,100]),  # iceLevel
            "Whole Milk"
        ]
        row += [random.randint(0,1) for _ in toppings]  # schema-aware topping values
        row.append(qty)  # quantity
        orderitems_writer.writerow(row)
        
        # Payments
        payments_writer.writerow([
            payment_id, order_id, random.choice(["Cash","Card","Mobile"]), "Completed"
        ])
        
        order_id += 1
        orderitem_id += 1
        payment_id += 1

# MenuItemIngredients
menuitemingredient_id = 1
with open("menuitemingredients.csv", "w", newline="") as f:
    writer = csv.writer(f)
    writer.writerow(["menuItemIngredientID", "menuItemID", "ingredientID", "ingredientQty"])
    
    for mid in range(1, NUM_MENUITEMS + 1):
        used_ingredients = random.sample(range(1, len(ingredients)+1), random.randint(3, 6))
        for ing in used_ingredients:
            qty = random.randint(1, 5)
            writer.writerow([menuitemingredient_id, mid, ing, qty])
            menuitemingredient_id += 1
