package com.example.a210139_fatin_drnazatul_project1

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.a210139_fatin_drnazatul_project1.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemsScreen(navController: NavHostController, viewModel: ResQBiteViewModel) {
    val colorScheme = MaterialTheme.colorScheme
    val myContributions by viewModel.myContributions.collectAsState()
    var foodName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    val categories = listOf("Bakery", "Canned", "Packed Meals", "Fruits", "Drinks", "Snacks")
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(categories[0]) }

    Scaffold(
        topBar = {
            Surface(
                color = colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shadowElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 20.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to Home",
                            tint = colorScheme.tertiary
                        )
                    }
                    Text(
                        text = "GIVE AWAY",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = colorScheme.tertiary
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Sharing is Caring!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = colorScheme.primary)
            Text("Fill in the details of the food you'd like to share.")

            OutlinedTextField(
                value = foodName,
                onValueChange = { foodName = it },
                label = { Text("Food Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = { selectedCategory = category; expanded = false }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Quantity / Portion") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Pick-up Location") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    if (foodName.isNotEmpty()) {
                        val finalLocation = if (location.isBlank()) "Bangi" else location
                        val categoryIcon = viewModel.getIconForCategory(selectedCategory)

                        viewModel.addMyFoodItem(
                            FoodItem(
                                title = foodName,
                                distance = "0.0km",
                                user = "Me",
                                location = finalLocation,
                                imageRes = categoryIcon
                            )
                        )
                        foodName = ""; quantity = ""; location = ""
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorScheme.tertiary)
            ) {
                Text("List My Food!", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Your Contribution Gallery", fontSize = 18.sp, fontWeight = FontWeight.Bold)

            if (myContributions.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                    Text("No items shared yet.", color = colorScheme.outline)
                }
            } else {
                myContributions.reversed().forEach { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
                        border = BorderStroke(1.dp, colorScheme.outlineVariant)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(12.dp).background(colorScheme.primary, CircleShape))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = item.title, fontWeight = FontWeight.Bold)
                                Text(text = "Available at ${item.location}", fontSize = 12.sp, color = colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    }
}