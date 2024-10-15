import android.service.autofill.OnClickAction
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kiosk.Entities.Employee
import com.example.kiosk.R
import com.example.kiosk.RepeatButtons.ExitButton

@Composable
fun EmployeeListScreen(viewModel: EmployeeViewModel = viewModel(),navigationBack: ()->Unit, navigationToVisitorInfoScreen: ()->Unit) {
    val employees by viewModel.employees.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    // Log the data observed from StateFlow
    Log.d("EmployeeScreen", "Employees observed: $employees")

    ExitButton(){navigationBack()}

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        Arrangement.Top,
        Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.sagenet_logo),
            contentDescription = "",
            modifier = Modifier.size(100.dp)
        )

        Text("Please select the person you are here to meet.", color = Color.Black, fontSize = 20.sp)

        Spacer(modifier = Modifier.padding(16.dp))

        SearchBar(
            query = searchQuery,
            onQueryChange = { query ->
                searchQuery = query
                viewModel.searchEmployees(query)
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (employees.isEmpty()) {
            Text("No employees found", style = MaterialTheme.typography.bodyLarge)
        } else {
            EmployeeList(employees = employees, navigationToVisitorInfoScreen)
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search employees") }
    )
}

@Composable
fun EmployeeList(employees: List<Employee>, navigationToVisitorInfoScreen: ()->Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        contentPadding = PaddingValues(32.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        items(employees) { employee ->
            EmployeeItem(employee = employee, navigationToVisitorInfoScreen)
        }
    }
}

@Composable
fun EmployeeItem(employee: Employee,navigationToVisitorInfoScreen: ()->Unit ) {
    Button(
        onClick = {navigationToVisitorInfoScreen()},

        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Black,
            containerColor = Color.White,
        ),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(0.5.dp, Color.Black),
        modifier = Modifier
            .height(150.dp)
            .padding(10.dp)

    ) {
        Text(
            text = "${employee.firstName} ${employee.lastName}",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth() // Ensures the text is centered within its container
        )
    }
}