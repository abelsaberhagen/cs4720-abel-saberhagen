Abel Saberhagen
eug8xn

AI Usage Notes:
AI was used to create the FootballData class from the JSON, as I thought it would be a tedious task to perform myself. I also used AI to create some of the UI Compose elements as I thought that would also be a tedious task that wouldn't give me any new deep insights into Kotlin. I also used it to help me find the current date (but did not use it to find the current college football week).

Although AI was not used to fetch the data from the JSON and transfer it to the ViewModel, I did use this website and found it very helpful
in guiding me through the process of using the data layer to fetch the API, and transfer it to the ViewModel, and then transfer it to the UI Compose: 
https://developer.android.com/codelabs/basic-android-kotlin-compose-getting-data-internet?continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fandroid-basics-compose-unit-5-pathway-1%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fbasic-android-kotlin-compose-getting-data-internet#0

The method behind finding the current college football week is to take the current day and subtract it from the starting Tuesday day of the football season. The API changes the week on Tuesdays, therefore, and the first Tuesday of the football season is September 1st, 2026. Then, subtract the current day from September 1st, which is the number of days since week 1 of the football season. Then divide that number by 7 since there are 7 days in a week. Then add 1 to account for the offset, as there are 0 days between September 1st and September 1st, but it is the first week. Then that number is typecasted to an Int to get the current week of the football season. 
