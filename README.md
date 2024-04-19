# Fetch & Display List Items App

## Overview

This project is an Android application designed to fetch JSON data from a given URL and display the data in a user-friendly manner. The app is built using the MVVM (Model-View-ViewModel) architecture, leveraging Jetpack Compose for UI rendering, Retrofit for network calls, and follows modern Android development practices.

## Features

- **Data Fetching**: Retrieves JSON data from a remote URL.
- **Data Processing**: Filters and sorts the data based on specific criteria.
- **User Interface**: Displays the processed data using Jetpack Compose.
- **Filter Functionality**: Allows users to filter displayed items based on `listId`.

## Key Components

### Model
- **Item.kt**: Defines the data structure corresponding to the JSON data.

### Repository
- **ItemRepository.kt**: Handles the network operations to fetch data from the remote URL.

### ViewModel
- **ItemViewModel.kt**: Contains the business logic to process the fetched data and set the data to the view layer. It filters out items with blank or null names and sorts the items first by `listId` and then by `name` within each list.
- **ItemViewModelFactory.kt**: Provides a factory to instantiate the `ItemViewModel` with necessary dependencies.

### View
- **MainActivity**: The main activity of the app where the UI is rendered using Jetpack Compose.

## Functionality

1. **Fetching Data**: The app starts by fetching JSON data from the specified URL using Retrofit.
2. **Data Processing**: The `ItemViewModel` then processes this data by:
   - Filtering out items with blank or null `name`.
   - Grouping items by `listId`.
   - Sorting groups by `listId` and items within each group by `name`.
   - **Note: Sorting of item names is done lexicographically as strings. If numerical sorting within names is needed, an alternative function in `ItemViewModel` can be used.**
3. **Displaying Data**: The processed data is displayed in a list format using Jetpack Compose in `MainActivity`.
4. **Filter Functionality**: A filter button at the top of the screen allows users to filter the items by `listId`. The default setting(`No Filter`) shows all items.

## Sorting Logic

- By default, items within each `listId` are sorted **lexicographically** by their `name` (e.g., "Item 276" appears before "Item 28").
- For numerical sorting based on the number in the `name`, an alternative sorting function is available in `ItemViewModel`. Replace the usage of `sortByName` function with `sortByNumericValueInName` function to achieve this result.

## How to Use the Filter Functionality

- Click the violet filter button at the top of the screen.
- Select a specific `listId` from the dropdown to filter the items, or choose "No filter" to display all items.

## Conclusion

This app showcases the use of modern Android development tools and practices to create a responsive and user-friendly application. It demonstrates data fetching, processing, and displaying using Kotlin, MVVM architecture and Jetpack Compose...
