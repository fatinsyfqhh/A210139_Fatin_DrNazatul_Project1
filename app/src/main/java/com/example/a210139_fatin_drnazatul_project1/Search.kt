package com.example.a210139_fatin_drnazatul_project1

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavHostController, viewModel: ResQBiteViewModel) {
    val colorScheme = MaterialTheme.colorScheme
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    val allFood by viewModel.publicFoodList.collectAsState()

    val searchResults = allFood.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
                it.user.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            Surface(
                color = colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shadowElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp)
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = colorScheme.tertiary)
                    }
                    Text(
                        text = "DISCOVER",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = colorScheme.tertiary
                    )
                    if (searchQuery.isNotEmpty() || selectedCategory != null) {
                        TextButton(onClick = {
                            searchQuery = ""
                            selectedCategory = null
                        }) {
                            Text("Clear", color = colorScheme.error)
                        }
                    } else {
                        Spacer(modifier = Modifier.width(48.dp))
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background)
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = { Text("Search food or colleges...") },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = colorScheme.tertiary) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, null)
                        }
                    }
                },
                shape = RoundedCornerShape(25.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.surface,
                    unfocusedContainerColor = colorScheme.surface
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                AnimatedContent(
                    targetState = searchQuery.isEmpty(),
                    label = "SearchTransition"
                ) { isBrowsing ->
                    if (isBrowsing) {
                        BrowseContent(
                            onCategoryClick = { searchQuery = it },
                            colorScheme = colorScheme
                        )
                    } else {
                        SearchResultsContent(
                            results = searchResults,
                            query = searchQuery,
                            colorScheme = colorScheme
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun BrowseContent(onCategoryClick: (String) -> Unit, colorScheme: ColorScheme) {
    val categories = listOf("Bakery", "Canned", "Packed Meals", "Fruits", "Drinks", "Snacks")
    Column {
        Card(
            modifier = Modifier.fillMaxWidth().height(150.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(2.dp, colorScheme.tertiary),
            colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceVariant.copy(alpha = 0.3f))
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.LocationOn,
                            null,
                            tint = colorScheme.tertiary,
                            modifier = Modifier.size(32.dp)
                        )
                        Text("Map View", fontWeight = FontWeight.Bold)
                        Text("12 items near UKM Bangi", fontSize = 12.sp)
                    }
                }
            }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Browse Categories", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                Card(
                    modifier = Modifier.clickable { onCategoryClick(category) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
                    border = BorderStroke(1.dp, colorScheme.outlineVariant)
                ) {
                    Box(
                        Modifier.fillMaxWidth().padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(category, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}
@Composable
fun SearchResultsContent(results: List<FoodItem>, query: String, colorScheme: ColorScheme) {
    Column {
        Text(
            text = "Results for \"$query\"",
            fontSize = 14.sp,
            color = colorScheme.outline,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (results.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No items found. Try another keyword!")
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(results) { item ->
                    ListItem(
                        headlineContent = { Text(item.title, fontWeight = FontWeight.Bold) },
                        supportingContent = { Text("Available at ${item.location}") },
                        leadingContent = {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = colorScheme.tertiaryContainer
                            ) {
                                Icon(
                                    Icons.Default.ShoppingCart,
                                    null,
                                    modifier = Modifier.padding(8.dp),
                                    tint = colorScheme.onTertiaryContainer
                                )
                            }
                        },
                        trailingContent = { Text(item.distance, fontSize = 12.sp) },
                        modifier = Modifier
                            .background(colorScheme.surface, RoundedCornerShape(12.dp))
                            .clickable { }
                    )
                }
            }
        }
    }
}
