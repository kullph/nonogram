package sf333.assignment.nonogram

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sf333.assignment.nonogram.ui.theme.NonogramTheme
import androidx.lifecycle.ViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NonogramTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.background
                ) {
                    mainFunc()
                }
            }
        }
    }
}

val level = listOf(
    listOf(0, 1, 1, 1, 0),
    listOf(1, 0, 0, 0, 1),
    listOf(1, 1, 1, 1, 1),
    listOf(1, 0, 0, 0, 1),
    listOf(1, 0, 0, 0, 1)
)

var tempClickedList = Array(level.size) { Array(level.size) { 0 } }
var heart = 3

fun createHint(level: List<List<Int>>): Pair<MutableList<MutableList<Int>>, MutableList<MutableList<Int>>> {
    val rowHints: MutableList<MutableList<Int>> = mutableListOf()
    val colHints: MutableList<MutableList<Int>> = MutableList(level[0].size) { mutableListOf() }

    // Calculate hints for rows
    for (i in level.indices) {
        var rowCount = 0
        val rowtemplist: MutableList<Int> = mutableListOf()

        for (j in level[i].indices) {
            if (level[i][j] == 1) {
                rowCount++
            } else if (rowCount > 0) {
                rowtemplist.add(rowCount)
                rowCount = 0
            }
        }
        if (rowCount > 0) {
            rowtemplist.add(rowCount)
        }
        rowHints.add(rowtemplist)
    }

    for (i in level[0].indices) {
        var colCount = 0
        val coltemplist: MutableList<Int> = mutableListOf()

        for (j in level.indices) {
            if (level[j][i] == 1) {
                colCount++
            } else if (colCount > 0) {
                coltemplist.add(colCount)
                colCount = 0
            }
        }
        if (colCount > 0) {
            coltemplist.add(colCount)
        }
        colHints[i] = coltemplist
    }

    return Pair(rowHints, colHints)
}




@Composable
fun mainFunc() {
    CreateBoard(level)
}

@Composable
fun Cross() {
    Canvas(
        modifier = Modifier
            .size(60.dp)
            .padding(10.dp)
    ) {
        drawLine(
            color = Color.Red,
            strokeWidth = 15f,
            cap = StrokeCap.Round,
            start = Offset(0f, 0f),
            end = Offset(size.width, size.height)
        )
        drawLine(
            color = Color.Red,
            strokeWidth = 15f,
            cap = StrokeCap.Round,
            start = Offset(0f, size.height),
            end = Offset(size.width, 0f)
        )
    }
}

@Composable
fun Filled() {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        drawRect(
            color = Color.DarkGray,
            size = Size(size.width, size.height),
            topLeft = Offset(0f, 0f),
        )
    }
}

@Composable
fun CreateCell(
    idx: String,
    ans: Int
) {
    var isClicked by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .clickable {
                isClicked = true
            }
            .background(
                color = if (isClicked) Color.Transparent else Color.LightGray
            )
            .aspectRatio(1f)
    ) {
        Text(
            text = "${ans.toString()} \n ${idx}"
        )

        if (isClicked) {
            if (heart <= 0) {
                tempClickedList = Array(level.size) { Array(level.size) { 0 } }
                heart = 3
                println("DIE")
            }
            tempClickedList[Character.getNumericValue(idx[0])][Character.getNumericValue(idx[1])] =
                1
        }
        if (ans == 0 && tempClickedList[Character.getNumericValue(idx[0])][Character.getNumericValue(
                idx[1]
            )] == 1
        ) {
            Cross()
            heart -= 1
        } else if (ans == 1 && tempClickedList[Character.getNumericValue(idx[0])][Character.getNumericValue(
                idx[1]
            )] == 1
        ) {
            Filled()
        }

        // auto logic
//        for (i in 1..level.size) {
//            var HorizontalScore = 0
//            var VerticalScore = 0
//
//            for (j in 1..level.size){
//                if (level[i][j] == 1 && tempClickedList[i][j] == 0){
//                    HorizontalScore -= 1
//                }
//
//                if (level[j][i] == 1 && tempClickedList[j][i] == 0){
//                    VerticalScore -= 1
//                }
//            }
//            if (HorizontalScore >= 0) {
//                tempClickedList[i] = Array(level.size) { 1 }
//            }
//            if (VerticalScore >= 0){
//                for (j in 1..level.size){
//                    tempClickedList[j][i] = 1
//                }
//            }
//        }

    }
}

@Composable
fun CreateBoard(
    level: List<List<Int>>,
    modifier: Modifier = Modifier
) {
    val hintsPair = createHint(level)

    val rowHints = hintsPair.first
    val colHints = hintsPair.second

    Column (
        modifier = Modifier
            .border(1.dp, Color.Gray)
    ){
        Row (
            modifier = Modifier
                .border(1.dp, Color.Gray)
        ){
            Column (
                modifier = Modifier
                    .width(300.dp/level.size)
            ){
                Text("${heart}")
            }
            Column (
                modifier = Modifier
            ){
                LazyRow(
                    modifier = Modifier
                ) {
                    items(colHints) { col ->
                        LazyColumn(
                            modifier = Modifier
                                .border(1.dp, Color.Gray)
                                .width(300.dp / level.size)
                        ){
                            items(col) { item ->
                                Text(text = "$item")
                            }
                        }
                    }
                }
            }
        }
        Row (
            modifier = Modifier
                .border(1.dp, Color.Gray)
        ){
            LazyColumn (
                modifier = Modifier
                    .width(300.dp / level.size)
            ){
                items(rowHints) { row ->
                    LazyRow (
                        modifier = Modifier
                            .height(300.dp / level.size)
                    ){
                        items(row) { item ->
                            Text(text="$item")
                        }
                    }
                }
            }
            Column (
                modifier = Modifier
                    .border(1.dp, Color.Gray)
                    .height(300.dp)
            ){
                Box(
                    modifier = Modifier
                        .border(1.dp, Color.Gray)
                        .size(width = 300.dp, height = 300.dp)
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(level.size),
                        modifier = modifier,
                        contentPadding = PaddingValues(5.dp),
                        verticalArrangement = Arrangement.spacedBy(3.dp),
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        itemsIndexed(level.flatten()) { index, cell ->
                            CreateCell(
                                idx = (index / level.size).toString() + (index % level.size).toString(),
                                ans = cell
                            )
                        }
                    }
                }
            }
        }
    }





}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NonogramTheme {
        mainFunc()
    }
}