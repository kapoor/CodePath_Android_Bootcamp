
Wiki: https://github.com/thecodepath/android_guides/wiki/RottenTomatoes-Networking-Tutorial

Idea: This shows a design pattern for building Android apps that call RESTful services  

Notes: 
- There should be a client (RottenTomatoesClient) containing methods with names same as endpoint
  names and parameters = query parameters to the end point
- There should be model (BoxOfficeMovie) with methods to create an array of objects from the results JSON returned by the client
- There should be an adapter to display the model object on the UI 
- The main activity (BoxOfficeActivity) should
    -- call the client asynchronously and get the JSON results
    -- Pass the JSON results to the model and get an array of objects from it
    -- Pass the objects to the adapter to display the results in the UI