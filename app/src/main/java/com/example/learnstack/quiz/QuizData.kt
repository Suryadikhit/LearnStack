package com.example.learnstack.quiz

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)

val quizzes = mapOf(
    "HTML" to listOf(
        QuizQuestion(
            "What does HTML stand for?",
            listOf(
                "HyperText Markup Language",
                "Home Tool Markup Language",
                "Hyperlinks and Text Markup Language",
                "Hyper Transfer Markup Language"
            ),
            0
        ),
        QuizQuestion(
            "Which tag is used for the largest heading?",
            listOf("<h6>", "<h1>", "<header>", "<h2>"),
            1
        ),
        QuizQuestion(
            "What is the correct HTML tag for inserting a line break?",
            listOf("<br>", "<break>", "<lb>", "<b>"),
            0
        ),
        QuizQuestion(
            "Which HTML element is used to define the navigation links?",
            listOf("<nav>", "<navigate>", "<links>", "<menu>"),
            0
        ),
        QuizQuestion(
            "What is the correct HTML tag to create a hyperlink?",
            listOf("<a>", "<link>", "<hyperlink>", "<href>"),
            0
        ),
        QuizQuestion(
            "Which tag is used to define an unordered list?",
            listOf("<ol>", "<ul>", "<li>", "<list>"),
            1
        ),
        QuizQuestion(
            "Which tag is used to define a table?",
            listOf("<table>", "<tab>", "<grid>", "<t>"),
            0
        ),
        QuizQuestion(
            "How can you make a list that lists items as bullet points?",
            listOf("<ol>", "<ul>", "<list>", "<li>"),
            1
        ),
        QuizQuestion(
            "What tag is used to define the footer of a page?",
            listOf("<footer>", "<bottom>", "<end>", "<footer-content>"),
            0
        ),
        QuizQuestion(
            "Which attribute is used to specify an image source in HTML?",
            listOf("src", "alt", "image", "source"),
            0
        ),
        QuizQuestion(
            "Which tag is used to define an ordered list?",
            listOf("<ul>", "<ol>", "<li>", "<list>"),
            1
        ),
        QuizQuestion(
            "What tag is used to create a line break in HTML?",
            listOf("<br>", "<lb>", "<break>", "<line>"),
            0
        ),
        QuizQuestion(
            "Which tag defines a section of a page?",
            listOf("<section>", "<article>", "<div>", "<header>"),
            0
        ),
        QuizQuestion(
            "Which HTML element is used for embedding a media file?",
            listOf("<video>", "<audio>", "<media>", "<embed>"),
            0
        ),
        QuizQuestion(
            "What does the `alt` attribute do in an image tag?",
            listOf(
                "Defines image height",
                "Provides a fallback text description",
                "Specifies image dimensions",
                "Links the image to a URL"
            ),
            1
        )
    ),
    "CSS" to listOf(
        QuizQuestion(
            "What does CSS stand for?",
            listOf(
                "Cascading Style Sheets",
                "Creative Style Sheets",
                "Computer Style Sheets",
                "Colorful Style Sheets"
            ),
            0
        ),
        QuizQuestion(
            "Which property is used to change the background color?",
            listOf("background-color", "color", "bgcolor", "background"),
            0
        ),
        QuizQuestion(
            "What is the correct CSS syntax to change the font size to 20px?",
            listOf("font-size: 20px;", "text-size: 20px;", "font: size 20px;", "size-font: 20px;"),
            0
        ),
        QuizQuestion(
            "Which property is used to center-align text?",
            listOf("text-align", "align-center", "text-center", "align-text"),
            0
        ),
        QuizQuestion(
            "How do you add a border with rounded corners?",
            listOf(
                "border-radius: 10px;",
                "round-border: 10px;",
                "border: round 10px;",
                "border-curve: 10px;"
            ),
            0
        ),
        QuizQuestion(
            "Which CSS property controls the text color?",
            listOf("color", "font-color", "text-color", "foreground-color"),
            0
        ),
        QuizQuestion(
            "What is the default value of the position property?",
            listOf("static", "relative", "absolute", "fixed"),
            0
        ),
        QuizQuestion(
            "How can you apply a CSS rule to an element with ID 'header'?",
            listOf("#header { }", ".header { }", "header { }", "*header { }"),
            0
        ),
        QuizQuestion(
            "Which property is used to make text bold?",
            listOf("font-weight", "font-bold", "text-weight", "bold"),
            0
        ),
        QuizQuestion(
            "What does the z-index property control?",
            listOf("Stacking order of elements", "Font size", "Opacity", "Element margin"),
            0
        ),
        QuizQuestion(
            "Which property is used to change the font of an element?",
            listOf("font-family", "font-style", "font-name", "font"),
            0
        ),
        QuizQuestion(
            "How can you make a background image repeat vertically only?",
            listOf(
                "background-repeat: repeat-y;",
                "background-repeat: repeat-x;",
                "background: repeat-vertical;",
                "background-vertical: true;"
            ),
            0
        ),
        QuizQuestion(
            "Which CSS property is used to control the space inside an element?",
            listOf("padding", "margin", "spacing", "border"),
            0
        ),
        QuizQuestion(
            "What value of display hides an element but keeps its space?",
            listOf(
                "visibility: hidden;",
                "display: none;",
                "display: invisible;",
                "visibility: none;"
            ),
            0
        ),
        QuizQuestion(
            "Which property is used to set the transparency level of an element?",
            listOf("opacity", "visibility", "alpha", "filter"),
            0
        )
    ),
    "JavaScript" to listOf(
        QuizQuestion(
            "What does JavaScript primarily run on?",
            listOf("Browser", "Server", "Operating System", "Database"),
            0
        ),
        QuizQuestion(
            "How do you write 'Hello World' in an alert box?",
            listOf(
                "alert('Hello World');",
                "msg('Hello World');",
                "alertBox('Hello World');",
                "message('Hello World');"
            ),
            0
        ),
        QuizQuestion(
            "What keyword is used to declare a variable?",
            listOf("var", "let", "const", "All of the above"),
            3
        ),
        QuizQuestion(
            "How do you write a function in JavaScript?",
            listOf(
                "function myFunc() { }",
                "func myFunc() { }",
                "myFunc function() { }",
                "function: myFunc { }"
            ),
            0
        ),
        QuizQuestion(
            "What does `typeof` operator return for `null`?",
            listOf("object", "null", "undefined", "string"),
            0
        ),
        QuizQuestion(
            "Which method is used to convert JSON to a JavaScript object?",
            listOf("JSON.parse()", "JSON.stringify()", "JSON.convert()", "JSON.objectify()"),
            0
        ),
        QuizQuestion(
            "Which symbol is used for comments in JavaScript?",
            listOf("//", "/* */", "<!-- -->", "#"),
            0
        ),
        QuizQuestion(
            "How can you check if two values are equal and of the same type?",
            listOf("===", "==", "=", "!=="),
            0
        ),
        QuizQuestion(
            "What is the output of `console.log(typeof NaN);`?",
            listOf("number", "NaN", "undefined", "string"),
            0
        ),
        QuizQuestion(
            "What will `Boolean('')` return?",
            listOf("false", "true", "null", "undefined"),
            0
        ),
        QuizQuestion(
            "Which event occurs when a user clicks on an element?",
            listOf("onclick", "onhover", "onpress", "onactivate"),
            0
        ),
        QuizQuestion(
            "What does `setInterval()` do?",
            listOf(
                "Executes a function repeatedly at a fixed interval",
                "Executes a function once after a delay",
                "Pauses execution",
                "Clears intervals"
            ),
            0
        ),
        QuizQuestion(
            "What is the purpose of `isNaN()` function?",
            listOf(
                "Checks if a value is not a number",
                "Converts value to a number",
                "Rounds a number",
                "Checks if a number is even"
            ),
            0
        ),
        QuizQuestion(
            "Which keyword is used to handle errors in JavaScript?",
            listOf("try", "catch", "throw", "All of the above"),
            3
        ),
        QuizQuestion(
            "How do you call a function named `myFunction`?",
            listOf("myFunction()", "call myFunction()", "myFunction;", "execute myFunction()"),
            0
        )
    ),
    "React" to listOf(
        QuizQuestion(
            "What is React?",
            listOf(
                "A JavaScript library for building UI",
                "A programming language",
                "A server-side framework",
                "A database system"
            ),
            0
        ),
        QuizQuestion(
            "Who developed React?",
            listOf("Facebook", "Google", "Microsoft", "Twitter"),
            0
        ),
        QuizQuestion(
            "What is JSX?",
            listOf(
                "A syntax extension for JavaScript",
                "A database query language",
                "A CSS preprocessor",
                "A type of React component"
            ),
            0
        ),
        QuizQuestion(
            "What is a React component?",
            listOf(
                "A reusable piece of UI",
                "A server-side function",
                "A styling tool",
                "A data storage system"
            ),
            0
        ),
        QuizQuestion(
            "What hook is used for managing state in functional components?",
            listOf("useState", "useEffect", "useReducer", "useContext"),
            0
        ),
        QuizQuestion(
            "What is the virtual DOM?",
            listOf(
                "A lightweight copy of the real DOM",
                "The actual DOM",
                "A feature for server-side rendering",
                "A browser API"
            ),
            0
        ),
        QuizQuestion(
            "What is the purpose of props in React?",
            listOf(
                "To pass data between components",
                "To manage state",
                "To render HTML",
                "To style components"
            ),
            0
        ),
        QuizQuestion(
            "What is a key used for in React lists?",
            listOf(
                "To uniquely identify list items",
                "To add items to a list",
                "To style the list",
                "To delete items from the list"
            ),
            0
        ),
        QuizQuestion(
            "What does the useEffect hook do?",
            listOf("Handles side effects", "Manages state", "Renders UI", "Fetches API data only"),
            0
        ),
        QuizQuestion(
            "Which method is used to update the state of a class component?",
            listOf("setState", "updateState", "changeState", "modifyState"),
            0
        ),
        QuizQuestion(
            "What is a higher-order component (HOC)?",
            listOf(
                "A function that takes a component and returns a new component",
                "A class-based component",
                "A CSS styling function",
                "A way to manage events"
            ),
            0
        ),
        QuizQuestion(
            "What is the purpose of the Context API in React?",
            listOf(
                "To manage global state",
                "To fetch API data",
                "To manage CSS styles",
                "To define routes"
            ),
            0
        ),
        QuizQuestion(
            "What does React.StrictMode do?",
            listOf(
                "Helps identify potential problems in the application",
                "Provides a testing framework",
                "Handles API requests",
                "Optimizes performance"
            ),
            0
        ),
        QuizQuestion(
            "What does the useRef hook do?",
            listOf(
                "Accesses a DOM element directly",
                "Manages state",
                "Runs side effects",
                "Defines routes"
            ),
            0
        ),
        QuizQuestion(
            "What is React Router?",
            listOf(
                "A library for routing in React apps",
                "A tool for database connection",
                "A CSS framework",
                "A state management library"
            ),
            0
        )
    ),
    "Python" to listOf(
        QuizQuestion(
            "Who developed Python?",
            listOf("Guido van Rossum", "Dennis Ritchie", "James Gosling", "Bjarne Stroustrup"),
            0
        ),
        QuizQuestion(
            "Which of the following is a Python data type?",
            listOf("List", "Tuple", "Dictionary", "All of the above"),
            3
        ),
        QuizQuestion(
            "What is the correct file extension for Python files?",
            listOf(".py", ".pt", ".python", ".p"),
            0
        ),
        QuizQuestion(
            "What does the `len()` function do?",
            listOf(
                "Returns the length of an object",
                "Calculates the sum of numbers",
                "Finds the largest number",
                "Returns a random number"
            ),
            0
        ),
        QuizQuestion(
            "Which keyword is used to define a function?",
            listOf("def", "function", "define", "fun"),
            0
        ),
        QuizQuestion(
            "What is Python's primary data structure for key-value pairs?",
            listOf("Dictionary", "List", "Tuple", "Set"),
            0
        ),
        QuizQuestion(
            "Which library is used for data manipulation in Python?",
            listOf("Pandas", "NumPy", "Matplotlib", "Scikit-learn"),
            0
        ),
        QuizQuestion(
            "What is the output of `2 ** 3` in Python?",
            listOf("8", "6", "9", "Error"),
            0
        ),
        QuizQuestion(
            "What does the `try` block do?",
            listOf(
                "Handles exceptions",
                "Executes a loop",
                "Initializes variables",
                "Defines a function"
            ),
            0
        ),
        QuizQuestion(
            "What is the correct syntax to import a library?",
            listOf(
                "import library_name",
                "load library_name",
                "library.import",
                "include library_name"
            ),
            0
        ),
        QuizQuestion("What is the output of `print(5 // 2)`?", listOf("2", "2.5", "3", "Error"), 0),
        QuizQuestion(
            "Which statement is used to stop a loop in Python?",
            listOf("break", "stop", "exit", "terminate"),
            0
        ),
        QuizQuestion(
            "What is the default value of the `end` parameter in the `print()` function?",
            listOf("Newline (\\n)", "Space", "Tab (\\t)", "None"),
            0
        ),
        QuizQuestion(
            "What is the purpose of the `self` parameter in class methods?",
            listOf(
                "Refers to the instance of the class",
                "Defines a static method",
                "Specifies a class-level variable",
                "Imports another module"
            ),
            0
        ),
        QuizQuestion(
            "Which Python keyword is used to create a class?",
            listOf("class", "def", "struct", "create"),
            0
        )
    ),
    "Django" to listOf(
        QuizQuestion(
            "Who developed Django?",
            listOf(
                "Adrian Holovaty and Simon Willison",
                "Guido van Rossum",
                "Mark Zuckerberg",
                "James Gosling"
            ),
            0
        ),
        QuizQuestion(
            "Django is a framework for which programming language?",
            listOf("Python", "Java", "C#", "Ruby"),
            0
        ),
        QuizQuestion(
            "What design pattern does Django follow?",
            listOf(
                "MVC (Model View Controller)",
                "MVP (Model View Presenter)",
                "MVVM (Model View ViewModel)",
                "MTV (Model Template View)"
            ),
            3
        ),
        QuizQuestion(
            "Which command is used to start a new Django project?",
            listOf(
                "django-admin startproject",
                "django start",
                "create django project",
                "python startproject"
            ),
            0
        ),
        QuizQuestion(
            "Which file is used to define URL patterns in Django?",
            listOf("urls.py", "views.py", "settings.py", "models.py"),
            0
        ),
        QuizQuestion(
            "What does the `makemigrations` command do in Django?",
            listOf(
                "Generates migration files",
                "Applies migrations",
                "Deletes the database",
                "Creates a new model"
            ),
            0
        ),
        QuizQuestion(
            "Which file is used for database settings in Django?",
            listOf("settings.py", "db.py", "models.py", "config.py"),
            0
        ),
        QuizQuestion(
            "What is the purpose of the `views.py` file?",
            listOf(
                "Defines the logic for handling requests",
                "Stores database models",
                "Handles user authentication",
                "Specifies middleware"
            ),
            0
        ),
        QuizQuestion(
            "What does the `migrate` command do in Django?",
            listOf(
                "Applies migrations to the database",
                "Generates migrations",
                "Deletes migrations",
                "Rolls back migrations"
            ),
            0
        ),
        QuizQuestion(
            "Which template engine is used by default in Django?",
            listOf("Django Template Language (DTL)", "Jinja2", "Mako", "Twig"),
            0
        ),
        QuizQuestion(
            "Which HTTP method is commonly used to retrieve data from a server in Django?",
            listOf("GET", "POST", "PUT", "DELETE"),
            0
        ),
        QuizQuestion(
            "How do you run the Django development server?",
            listOf(
                "python manage.py runserver",
                "django runserver",
                "run django server",
                "python server.py"
            ),
            0
        ),
        QuizQuestion(
            "What is the default database used by Django?",
            listOf("SQLite", "PostgreSQL", "MySQL", "MongoDB"),
            0
        ),
        QuizQuestion(
            "Which Django feature is used for user authentication?",
            listOf(
                "Django Authentication Framework",
                "Django Middleware",
                "Django Templates",
                "Django ORM"
            ),
            0
        ),
        QuizQuestion(
            "What is the purpose of Django's ORM?",
            listOf(
                "To interact with the database using Python code",
                "To create static files",
                "To manage middleware",
                "To handle HTTP requests"
            ),
            0
        )
    ),
    "AI" to listOf(
        QuizQuestion(
            "What does AI stand for?",
            listOf(
                "Artificial Intelligence",
                "Automated Interface",
                "Algorithmic Insights",
                "Advanced Integration"
            ),
            0
        ),
        QuizQuestion(
            "Who is known as the father of Artificial Intelligence?",
            listOf("John McCarthy", "Alan Turing", "Elon Musk", "Geoffrey Hinton"),
            0
        ),
        QuizQuestion(
            "What is the Turing Test used for?",
            listOf(
                "Testing a machine's ability to exhibit intelligent behavior",
                "Measuring processing speed",
                "Evaluating algorithm efficiency",
                "Analyzing user behavior"
            ),
            0
        ),
        QuizQuestion(
            "Which type of AI can perform specific tasks but lacks general intelligence?",
            listOf("Narrow AI", "Strong AI", "General AI", "Super AI"),
            0
        ),
        QuizQuestion(
            "What does NLP stand for in AI?",
            listOf(
                "Natural Language Processing",
                "Non-Linear Programming",
                "Network Learning Protocol",
                "Neutral Logic Programming"
            ),
            0
        ),
        QuizQuestion(
            "Which algorithm is commonly used in AI for decision-making trees?",
            listOf("ID3", "KNN", "SVM", "Naive Bayes"),
            0
        ),
        QuizQuestion(
            "What is the purpose of a neural network in AI?",
            listOf(
                "To mimic the human brain's functionality",
                "To store data",
                "To optimize queries",
                "To create graphics"
            ),
            0
        ),
        QuizQuestion(
            "Which AI domain focuses on teaching machines to learn from data?",
            listOf("Machine Learning", "Data Science", "Robotics", "Expert Systems"),
            0
        ),
        QuizQuestion(
            "What is an example of unsupervised learning?",
            listOf("Clustering", "Classification", "Regression", "Decision Trees"),
            0
        ),
        QuizQuestion(
            "Which AI field deals with planning and controlling robotic devices?",
            listOf("Robotics", "NLP", "Computer Vision", "Machine Learning"),
            0
        ),
        QuizQuestion(
            "What is a chatbot an example of?",
            listOf("NLP", "Supervised Learning", "Computer Vision", "Robotics"),
            0
        ),
        QuizQuestion(
            "Which company developed AlphaGo, an AI system?",
            listOf("DeepMind", "OpenAI", "Google AI", "IBM"),
            0
        ),
        QuizQuestion(
            "Which language is widely used for AI programming?",
            listOf("Python", "Java", "C++", "Ruby"),
            0
        ),
        QuizQuestion(
            "What does the term 'deep learning' refer to?",
            listOf(
                "Using multi-layered neural networks",
                "Using shallow networks",
                "Optimizing database queries",
                "Building rule-based systems"
            ),
            0
        ),
        QuizQuestion(
            "Which of these is an AI-powered assistant?",
            listOf("Siri", "MySQL", "Apache", "Photoshop"),
            0
        )
    ),
    "ML" to listOf(
        QuizQuestion(
            "What does ML stand for?",
            listOf("Machine Learning", "Modeling Language", "Modular Learning", "Managed Library"),
            0
        ),
        QuizQuestion(
            "Which type of learning involves labeled data?",
            listOf(
                "Supervised Learning",
                "Unsupervised Learning",
                "Reinforcement Learning",
                "Deep Learning"
            ),
            0
        ),
        QuizQuestion(
            "What is overfitting in Machine Learning?",
            listOf(
                "When a model performs well on training data but poorly on new data",
                "When a model performs well on all data",
                "When a model fails to train",
                "When a model has too few parameters"
            ),
            0
        ),
        QuizQuestion(
            "What is the goal of regression in ML?",
            listOf(
                "Predicting continuous values",
                "Classifying categories",
                "Clustering data points",
                "Optimizing algorithms"
            ),
            0
        ),
        QuizQuestion(
            "What is an example of a supervised learning algorithm?",
            listOf(
                "Linear Regression",
                "K-Means Clustering",
                "Apriori Algorithm",
                "Self-Organizing Maps"
            ),
            0
        ),
        QuizQuestion(
            "Which algorithm is commonly used for classification problems?",
            listOf("Support Vector Machines (SVM)", "K-Means", "Linear Regression", "Apriori"),
            0
        ),
        QuizQuestion(
            "What is a confusion matrix used for?",
            listOf(
                "Evaluating classification performance",
                "Clustering data",
                "Storing predictions",
                "Optimizing parameters"
            ),
            0
        ),
        QuizQuestion(
            "What is feature scaling?",
            listOf(
                "Normalizing data to bring all features to a similar scale",
                "Removing irrelevant features",
                "Adding more features",
                "Transforming categorical features"
            ),
            0
        ),
        QuizQuestion(
            "What does a decision tree do?",
            listOf(
                "Creates a tree-like model for decisions",
                "Clusters data",
                "Predicts time series",
                "Optimizes queries"
            ),
            0
        ),
        QuizQuestion(
            "What is the purpose of cross-validation?",
            listOf(
                "To evaluate a model's performance",
                "To train a model",
                "To visualize data",
                "To deploy a model"
            ),
            0
        ),
        QuizQuestion(
            "What is the primary goal of unsupervised learning?",
            listOf(
                "Discover hidden patterns in data",
                "Classify data into categories",
                "Predict continuous values",
                "Optimize performance metrics"
            ),
            0
        ),
        QuizQuestion(
            "What is a common technique for dimensionality reduction?",
            listOf(
                "Principal Component Analysis (PCA)",
                "KNN",
                "Decision Trees",
                "Gradient Boosting"
            ),
            0
        ),
        QuizQuestion(
            "Which type of learning uses rewards to guide actions?",
            listOf(
                "Reinforcement Learning",
                "Supervised Learning",
                "Unsupervised Learning",
                "Deep Learning"
            ),
            0
        ),
        QuizQuestion(
            "What is an epoch in machine learning?",
            listOf(
                "One complete pass through the training dataset",
                "A specific point in time",
                "A type of algorithm",
                "A method for gradient descent"
            ),
            0
        ),
        QuizQuestion(
            "Which library is widely used for Machine Learning?",
            listOf("Scikit-learn", "React", "Django", "Bootstrap"),
            0
        )
    ),
    "Data Science" to listOf(
        QuizQuestion(
            "What is Data Science?",
            listOf(
                "An interdisciplinary field focusing on extracting insights from data",
                "A study of database management systems",
                "A programming language for data analysis",
                "A tool for creating dashboards"
            ),
            0
        ),
        QuizQuestion(
            "Which language is most commonly used in Data Science?",
            listOf("Python", "Java", "C++", "Ruby"),
            0
        ),
        QuizQuestion(
            "What is the primary goal of Data Science?",
            listOf(
                "To extract actionable insights from data",
                "To develop applications",
                "To create machine learning models only",
                "To store data in databases"
            ),
            0
        ),
        QuizQuestion(
            "Which library is used for data visualization in Python?",
            listOf("Matplotlib", "NumPy", "Pandas", "SciPy"),
            0
        ),
        QuizQuestion(
            "What does the term 'Big Data' refer to?",
            listOf(
                "Large volumes of structured and unstructured data",
                "Data stored in a database",
                "Small datasets",
                "Unimportant data"
            ),
            0
        ),
        QuizQuestion(
            "What is the role of a data scientist?",
            listOf(
                "To analyze and interpret complex data",
                "To develop websites",
                "To maintain servers",
                "To test software"
            ),
            0
        ),
        QuizQuestion(
            "Which Python library is used for data manipulation?",
            listOf("Pandas", "Matplotlib", "Seaborn", "TensorFlow"),
            0
        ),
        QuizQuestion(
            "What does EDA stand for in Data Science?",
            listOf(
                "Exploratory Data Analysis",
                "Effective Data Analysis",
                "Enhanced Data Analytics",
                "Extended Data Algorithms"
            ),
            0
        ),
        QuizQuestion(
            "Which algorithm is commonly used for classification problems in Data Science?",
            listOf("Logistic Regression", "Linear Regression", "K-Means", "PCA"),
            0
        ),
        QuizQuestion(
            "What is a data pipeline?",
            listOf(
                "A series of steps to process and analyze data",
                "A software library for data analysis",
                "A database management system",
                "A storage technique"
            ),
            0
        ),
        QuizQuestion(
            "What is the purpose of a Jupyter Notebook?",
            listOf(
                "To write and run code interactively",
                "To store datasets",
                "To create machine learning models",
                "To manage databases"
            ),
            0
        ),
        QuizQuestion(
            "What is feature engineering?",
            listOf(
                "Transforming raw data into meaningful features for models",
                "Scaling data values",
                "Removing irrelevant data",
                "Creating visualizations"
            ),
            0
        ),
        QuizQuestion(
            "What is the term for missing values in a dataset?",
            listOf("Null or NA values", "Outliers", "Redundant data", "Scaled data"),
            0
        ),
        QuizQuestion(
            "Which library is commonly used for numerical computations in Python?",
            listOf("NumPy", "Pandas", "Scikit-learn", "Matplotlib"),
            0
        ),
        QuizQuestion(
            "What is a confusion matrix used for in Data Science?",
            listOf(
                "To evaluate the performance of classification models",
                "To cluster data points",
                "To handle missing values",
                "To preprocess datasets"
            ),
            0
        )
    )


)

