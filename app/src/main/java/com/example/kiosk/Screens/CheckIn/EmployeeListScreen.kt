import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kiosk.Entities.Employee
import com.example.kiosk.R
import com.example.kiosk.RepeatButtons.ExitButton
import java.util.UUID

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.zIndex

@Composable
fun EmployeeListScreen(viewModel: EmployeeViewModel = viewModel(), navigationBack: () -> Unit, navigationToVisitorInfoScreen: (String, String) -> Unit) {
    val employees by viewModel.employees.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    // Log the data observed from StateFlow
    Log.d("EmployeeScreen", "Employees observed: $employees")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { focusManager.clearFocus() }
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ExitButton(
                onClick = { navigationBack() },
                modifier = Modifier
                    .align(Alignment.Start)
                    .zIndex(1f)
            )

            Image(
                painter = painterResource(id = R.drawable.sagenet_logo),
                contentDescription = "",
                modifier = Modifier.size(100.dp)
            )

            Text(
                "Please select the person you are here to meet.",
                color = Color.Black,
                fontSize = 20.sp
            )

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
                EmployeeList(employees = employees) { personOfInterest, personOfInterestEmail ->
                    navigationToVisitorInfoScreen(personOfInterest, personOfInterestEmail)
                }
            }
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
fun EmployeeList(employees: List<Employee>, navigationToVisitorInfoScreen: (String, String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        contentPadding = PaddingValues(32.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        items(employees) { employee ->
            EmployeeItem(
                employee = employee,
                navigationToVisitorInfoScreen = { personOfInterest, personOfInterestEmail ->
                    navigationToVisitorInfoScreen(personOfInterest, personOfInterestEmail)
                }
            )
        }
    }
}


@Composable
fun EmployeeItem(employee: Employee, navigationToVisitorInfoScreen: (String, String)->Unit) {
    Button(
        onClick = {
            val personOfInterest = employee.firstName + " " + employee.lastName
            val personOfInterestEmail = employee.email
            navigationToVisitorInfoScreen(personOfInterest, personOfInterestEmail)
        },
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
            modifier = Modifier.fillMaxWidth()
        )
    }
}