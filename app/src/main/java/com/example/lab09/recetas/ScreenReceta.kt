package com.example.lab09.recetas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

@Composable
fun ScreenReceta(navController: NavHostController, viewModel: RecipeDetailViewModel = viewModel()) {
    val estado by viewModel.uiState.collectAsState()

    when (val e = estado) {
        is DetalleUiState.Cargando -> Box(Modifier.fillMaxSize().background(Crema), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Naranja)
        }
        is DetalleUiState.Error -> Box(Modifier.fillMaxSize().background(Crema), contentAlignment = Alignment.Center) {
            Text(e.mensaje)
        }
        is DetalleUiState.Listo -> DetalleReceta(e.receta) { navController.popBackStack() }
    }
}

@Composable
private fun DetalleReceta(receta: Recipe, onVolver: () -> Unit) {
    LazyColumn(Modifier.fillMaxSize().background(Crema)) {
        item {
            Box {
                AsyncImage(
                    model = receta.image,
                    contentDescription = receta.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(280.dp).background(Color(0xFFFFE2CF))
                )
                IconButton(
                    onClick = onVolver,
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(12.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f))
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = TextoOscuro)
                }
            }
        }
        item {
            Column(
                Modifier
                    .offset(y = (-24).dp)
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .background(Crema)
                    .padding(20.dp)
            ) {
                Text(receta.cuisine.uppercase() + "  ·  " + receta.mealType.joinToString(", "),
                    color = NaranjaOscuro, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(receta.name, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = TextoOscuro)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(18.dp))
                    Text(" ${receta.rating}", fontWeight = FontWeight.Bold, color = TextoOscuro)
                }
                Spacer(Modifier.height(14.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    Dato("⏱", "${receta.prepTimeMinutes + receta.cookTimeMinutes} min", Modifier.weight(1f))
                    Dato("🍽", "${receta.servings} porc.", Modifier.weight(1f))
                    Dato("🔥", "${receta.calories} kcal", Modifier.weight(1f))
                    Dato("📊", receta.difficulty, Modifier.weight(1f))
                }
                Spacer(Modifier.height(20.dp))
                Text("Ingredientes", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = TextoOscuro)
                Spacer(Modifier.height(8.dp))
                receta.ingredients.forEach { ing ->
                    Row(Modifier.padding(vertical = 3.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(8.dp).clip(CircleShape).background(Naranja))
                        Text("  $ing", color = TextoOscuro, fontSize = 15.sp)
                    }
                }
                Spacer(Modifier.height(20.dp))
                Text("Preparación", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = TextoOscuro)
                Spacer(Modifier.height(8.dp))
            }
        }
        itemsIndexed(receta.instructions) { i, paso ->
            Row(Modifier.padding(start = 20.dp, end = 20.dp, bottom = 12.dp).offset(y = (-24).dp)) {
                Box(
                    Modifier.size(28.dp).clip(CircleShape).background(Naranja),
                    contentAlignment = Alignment.Center
                ) { Text("${i + 1}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp) }
                Text(paso, color = TextoOscuro, fontSize = 15.sp, modifier = Modifier.padding(start = 12.dp, top = 3.dp))
            }
        }
    }
}

@Composable
private fun Dato(icono: String, texto: String, modifier: Modifier = Modifier) {
    Column(
        modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(icono, fontSize = 18.sp)
        Text(texto, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextoOscuro, maxLines = 1)
    }
}
