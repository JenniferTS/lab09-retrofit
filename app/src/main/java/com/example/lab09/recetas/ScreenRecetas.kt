package com.example.lab09.recetas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

val Naranja = Color(0xFFFF7A3D)
val NaranjaOscuro = Color(0xFFE85D1F)
val Crema = Color(0xFFFFF6EE)
val TextoOscuro = Color(0xFF2B1A12)

@Composable
fun ScreenRecetas(navController: NavHostController, viewModel: RecipesViewModel = viewModel()) {
    val estado by viewModel.uiState.collectAsState()
    val busqueda by viewModel.busqueda.collectAsState()

    Column(Modifier.fillMaxSize().background(Crema)) {
        // Encabezado
        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                .background(Brush.horizontalGradient(listOf(Naranja, NaranjaOscuro)))
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            Text("Hola, Sharon 👋", color = Color.White.copy(alpha = 0.9f), fontSize = 15.sp)
            Text("¿Qué cocinamos hoy?", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(14.dp))
            TextField(
                value = busqueda,
                onValueChange = viewModel::onBusquedaChange,
                placeholder = { Text("Buscar receta (pizza, pasta...)") },
                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        when (val e = estado) {
            is RecetasUiState.Cargando -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Naranja)
            }
            is RecetasUiState.Error -> Column(
                Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(e.mensaje, color = TextoOscuro)
                Spacer(Modifier.height(12.dp))
                Button(onClick = viewModel::cargar, colors = ButtonDefaults.buttonColors(containerColor = Naranja)) {
                    Text("Reintentar")
                }
            }
            is RecetasUiState.Listo -> LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item(span = { GridItemSpan(2) }) {
                    Text(
                        if (busqueda.isBlank()) "Recetas populares (${e.recetas.size})" else "Resultados (${e.recetas.size})",
                        fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextoOscuro
                    )
                }
                if (e.recetas.isEmpty()) {
                    item(span = { GridItemSpan(2) }) {
                        Text("No encontré recetas con \"$busqueda\" 😕", color = TextoOscuro.copy(alpha = 0.7f))
                    }
                }
                items(e.recetas, key = { it.id }) { receta ->
                    TarjetaReceta(receta) { navController.navigate("receta/${receta.id}") }
                }
            }
        }
    }
}

@Composable
fun TarjetaReceta(receta: Recipe, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
    ) {
        Box {
            AsyncImage(
                model = receta.image,
                contentDescription = receta.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(130.dp).background(Color(0xFFFFE2CF))
            )
            Row(
                Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.92f))
                    .padding(horizontal = 8.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(14.dp))
                Text(" ${receta.rating}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextoOscuro)
            }
        }
        Column(Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
            Text(
                receta.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextoOscuro,
                maxLines = 2, overflow = TextOverflow.Ellipsis, minLines = 2
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "⏱ ${receta.prepTimeMinutes + receta.cookTimeMinutes} min  ·  ${receta.cuisine}",
                fontSize = 12.sp, color = TextoOscuro.copy(alpha = 0.6f), maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        }
    }
}
