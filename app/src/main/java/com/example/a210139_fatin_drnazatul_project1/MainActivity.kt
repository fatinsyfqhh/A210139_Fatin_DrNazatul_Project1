package com.example.a210139_fatin_drnazatul_project1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize // lab03 (animation task)
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.* // basic layout tools (calls all)
import androidx.compose.foundation.layout.* // sizing and spacing tools (dp, fillMaxSize)
import androidx.compose.foundation.lazy.LazyRow // horizontal scrollable lists
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.* // 3 design elements (cards, navigation bars, texts)
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a210139_fatin_drnazatul_project1.ui.theme.A210139_Fatin_DrNazatul_Project1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            A210139_Fatin_DrNazatul_Project1Theme {
                val navController = rememberNavController()
                val viewModel: ResQBiteViewModel = viewModel()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = { ResQBiteBottomNavigation(navController) }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = "home",
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable("home") { ResQBiteHomeScreen(navController, viewModel) }
                            composable("foodDetail/{foodName}") { backStackEntry ->
                                val foodName = backStackEntry.arguments?.getString("foodName") ?: ""
                                val foodItem = viewModel.publicFoodList.collectAsState().value.find { it.title == foodName }
                                    ?: viewModel.myContributions.collectAsState().value.find { it.title == foodName }
                                foodItem?.let {
                                    FoodDetailScreen(item = it, navController = navController)
                                }
                            }
                            composable("urgent_items") { UrgentItemsScreen(navController) }
                            composable("add_items") { AddItemsScreen(navController, viewModel) }
                            composable("messages") { MessagesScreen(navController = navController) }
                            composable("search") { SearchScreen(navController, viewModel) }
                            composable("community") { CommunityScreen(navController = navController, viewModel = viewModel) }
                        }
                    }
                }
            }
        }
    }
}

// layout section
@Composable
fun ResQBiteHomeScreen(navController: NavHostController, viewModel: ResQBiteViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    var activeFilter by remember { mutableStateOf("") }

    val publicFood by viewModel.publicFoodList.collectAsState()
    val myFood by viewModel.myContributions.collectAsState()
    val allFood = myFood + publicFood //this line make it add mycontribution in the list too
    // filter the list based on the activeFilter state
    val filteredFood = allFood.filter {
        it.title.contains(activeFilter, ignoreCase = true)
    }

    // access color
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .verticalScroll( state = rememberScrollState()) // make it scrollable up and down
    ) {
        // 1. header section
        Surface(
            color = colorScheme.surfaceVariant.copy(alpha = 0.5f),
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row( // horizontal (side by side)
                modifier = Modifier
                    .padding(16.dp) // make the content has space between the edges
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image( // ResQBite logo
                            painter = painterResource(id = R.drawable.resqlogo),
                            contentDescription = "logo",
                            modifier = Modifier.size(50.dp)
                        )

                        Text(text = "ResQBite", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = colorScheme.primary)
                    }

                    Spacer(modifier = Modifier.width(5.dp))

                    Column {
                        Text(text = "Listing within 25km", fontSize = 14.sp, color = colorScheme.onSurfaceVariant)
                        Text(text = "📍 Bangi ⌄", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Row {
                    Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("☰", fontSize = 24.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp)) // spaces between the header and the next section

        // lab02 improvements (search bar)
        // lab03 improvements (search bar with card to make it more organized)
        Card(
            modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceVariant.copy(alpha = 0.3f)),
            border = BorderStroke(1.dp, colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.search_hint),
                                fontSize = 12.sp,
                                maxLines = 1
                            )
                        },
                        modifier = Modifier
                            .weight(1f) // takes up available space
                            .height(52.dp),
                        shape = RoundedCornerShape(25.dp),
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = colorScheme.tertiary) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = colorScheme.surface,
                            unfocusedContainerColor = colorScheme.surface
                        )
                    )

                    Button(
                        onClick = { activeFilter = searchQuery },
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorScheme.tertiary),
                        modifier = Modifier.height(52.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        Text(stringResource(R.string.search_button), fontSize = 14.sp, color = colorScheme.onTertiary)
                    }
                }
                if (activeFilter.isNotEmpty()) { // view all button
                    TextButton(
                        onClick = {
                            activeFilter = ""
                            searchQuery = ""
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Clear Search & View All", color = colorScheme.tertiary, fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // filtered food list
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(text = "Suggested Food For You", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            if (filteredFood.isEmpty()) {
                Text(
                    "No food found matching \"$activeFilter\"",
                    color = colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(filteredFood) { food ->
                        FoodCard(
                            title = food.title,
                            distance = food.distance,
                            user = food.user,
                            location = food.location,
                            imageRes = food.imageRes,
                            onClick = { navController.navigate("foodDetail/${food.title}") }
                        )
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {

            Spacer(modifier = Modifier.height(24.dp))

            // 3. 'Take or Give' section
            Text(text = "Take or Give", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) { // horizontal scrollable with the space of 16 pixels between each box
                item {
                    TakeGiveCard(
                        text = "Expiring soon, grab now!!",
                        emoji = "⏰🔥",
                        containerColor = colorScheme.primaryContainer,
                        contentColor = colorScheme.onPrimaryContainer,
                        onClick = { navController.navigate("urgent_items") }
                    )
                }
                item {
                    TakeGiveCard(
                        text = "Wait... I can give that away?",
                        emoji = "👀👀",
                        containerColor = colorScheme.tertiaryContainer,
                        contentColor = colorScheme.onTertiaryContainer,
                        onClick = { navController.navigate("add_items") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 5. 'Events Rescue Pile' section
            Text(text = "Events Rescue Pile", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(12.dp))

            // updated with task 3 animations inside the component
            EventRescueCard(
                eventName = "EduTalk",
                location = "DECTAR",
                foodItem = "Packed Nasi Ayam Merah",
                quantity = "17 packages remaining"
            )

            Spacer(modifier = Modifier.height(16.dp))

            EventRescueCard(
                eventName = "Charity Run 2026",
                location = "Panggung Seni",
                foodItem = "Banana & Isotonic Drinks",
                quantity = "30 items available"
            )
        }
    }
}

// design section
// lab03 improvements (animation)
@Composable
fun EventRescueCard(eventName: String, location: String, foodItem: String, quantity: String) {
    val colorScheme = MaterialTheme.colorScheme
    var isExpanded by remember { mutableStateOf(false) } // state for animation

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded } // expand to show infos
            .animateContentSize( //so that box tu expand ikut size content dalam bpx tu je
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        border = BorderStroke(2.dp, color = colorScheme.tertiary) // outline border
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = eventName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = colorScheme.tertiary, // title color
                    modifier = Modifier.weight(1f)
                )
                // visual indicator for expandability
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = colorScheme.tertiary
                )
            }

            if (isExpanded) { // detailed section reveals during animation
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "📍 $location", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = colorScheme.outlineVariant) // line separator
                Text(text = "Food: $foodItem", fontSize = 16.sp)
                Text(text = "Quantity: $quantity", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = colorScheme.error)
            }
        }
    }
}

@Composable
fun TakeGiveCard(text: String, emoji: String, containerColor: Color, contentColor: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(width = 150.dp, height = 150.dp) // size of the box
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 30.sp)
            Spacer(modifier = Modifier.height(12.dp)) // space between emojis and texts
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = contentColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun FoodCard(title: String, distance: String, user: String, location: String, imageRes: Int, onClick: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = Modifier.width(180.dp).clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // shadow below the box
    ) {
        Column {
            Box {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = title,
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    contentScale = ContentScale.Crop
                )
                Surface( // little box for the distance text
                    color = colorScheme.surfaceVariant.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.align(Alignment.BottomEnd).padding(4.dp)
                ) {
                    Text(text = "📍 $distance", fontSize = 10.sp, modifier = Modifier.padding(4.dp), color = colorScheme.onSurfaceVariant)
                }
            }
            Column(modifier = Modifier.padding(8.dp)) { // texts section under the image
                Text(text = title, fontWeight = FontWeight.Medium, color = colorScheme.onSurface)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // profile circle uses secondary theme color
                    Box(modifier = Modifier.size(18.dp).background(colorScheme.secondaryContainer, RoundedCornerShape(9.dp)))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = user, fontSize = 12.sp, color = colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
fun ResQBiteBottomNavigation(navController: NavHostController) {
    val colorScheme = MaterialTheme.colorScheme
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // containerColor = colorScheme.surface ensures the bar turns dark in dark mode
    NavigationBar(containerColor = colorScheme.surface) {
        val items = listOf(
            Triple("Home", "home", Icons.Default.Home),
            Triple("Search", "search", Icons.Default.Search),
            Triple("Add", "add_items", Icons.Default.Add),
            Triple("Community", "community", Icons.Default.Person),
            Triple("Messages", "messages", Icons.Default.Email)
        )

        items.forEach { (label, route, icon) ->
            NavigationBarItem(
                icon = { Icon(icon, contentDescription = null) },
                label = { Text(label) },
                selected = currentRoute == route,
                onClick = {
                    if (currentRoute != route) {
                        navController.navigate(route) {
                            popUpTo("home") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colorScheme.primary,
                    selectedTextColor = colorScheme.primary,
                    indicatorColor = colorScheme.primaryContainer.copy(alpha = 0.4f),
                    unselectedIconColor = colorScheme.onSurfaceVariant,
                    unselectedTextColor = colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

// light mode preview
@Preview(showBackground = true, name = "Light Mode")
@Composable
fun LightPreview() {
    val navController = rememberNavController()
    val viewModel: ResQBiteViewModel = viewModel() //reconnect the data when rotate
    A210139_Fatin_DrNazatul_Project1Theme(darkTheme = false) {
        Scaffold(bottomBar = { ResQBiteBottomNavigation(navController) }) { p ->
            Box(modifier = Modifier.padding(p)) { ResQBiteHomeScreen(navController, viewModel) }
        }
    }
}

// dark mode preview
@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun DarkPreview() {
    val navController = rememberNavController()
    val viewModel: ResQBiteViewModel = viewModel()
    A210139_Fatin_DrNazatul_Project1Theme(darkTheme = true) {
        Scaffold(bottomBar = { ResQBiteBottomNavigation(navController) }) { p ->
            Box(modifier = Modifier.padding(p)) { ResQBiteHomeScreen(navController, viewModel) }
        }
    }
}