package com.example.proyectoaplicativo.presentation.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.proyectoaplicativo.presentation.navigation.BottomNavItem
import com.example.proyectoaplicativo.presentation.navigation.RutasNav

@Composable
fun AppTopBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val ruta = navBackStackEntry.value?.destination?.route
    val titulo = RutasNav.getTitulo(ruta)
    val mostrarBotonBack = ruta?.startsWith("detalle") == true || ruta?.startsWith("form") == true

    val colorPrincipal = Color(0xFF0D47A1)

    Column{
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 10.dp, top = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (mostrarBotonBack){
                IconButton(onClick = {navController.popBackStack()}) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atras",
                        tint = colorPrincipal,
                        modifier = Modifier.size(28.dp)
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.Cloud,
                    contentDescription = "Logo",
                    tint = colorPrincipal,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = titulo,
                color = colorPrincipal,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = {
                navController.navigate(BottomNavItem.Buscar.route)}) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = colorPrincipal
                )
            }

            IconButton(onClick = { navController.navigate(RutasNav.LOGIN){popUpTo(0) {inclusive = true}
            }
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Cerrar Sesion",
                    tint = Color(0xFFB71C1C)
                )
            }
            }
        HorizontalDivider(thickness = 1.dp, color = Color(0xFFCFD8DC))

        }
    }

