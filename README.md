# MAD_ASG

# Team Members:
* Avendano Kiara Kemuel Nacasas S10219186A
* Chan Woon Ning Alethea S10221902D
* Chong Xin Le S10221973D
* Sherylyn Tan Qin Xi S10222278A
* Yeo Sze Yun, Gwyneth S10222843C

## Introduction

Introducing '***Producti-vibe***', a powerful productivity app designed to help you stay organized, productive and destress in an enjoyable way. With Producti-Vibe, you can take control of your productivity and manage your tasks efficiently to accomplish your goals with ease. Not only that, Producti-Vibe understands the importance of maintaining a healthy work-life balance, which is why our app has a destress page for users to access after being productive for a period of time to recharge and relieve stress. With Producti-vibe as your ultimate productivity companion, you will be able to boost your productivity and enhance your overall well-being.

So why settle for mediocrity when you achieve greatness with this powerful productivity app? 

Link to app on google play store: https://play.google.com/store/apps/details?id=sg.edu.np.mad.productivibe_

##  Features!!! 🐔

### Stage 1


| Feature | Description | Concept | In-charge |
| :------ | :---------- | :-------- | :-------- | 
| Responsive layout | UI changes according to display size | Responsive Layout | Kiara |
| Destress |  Videos/pictures to view for a 1 minute to help to destress | Fragments, Media (photos, videos, gif), Recycler view, Event handling | Gwyneth |
| Checklist / To-do list | Viewing and checking tasks for users to know what to do | Database, Shared preferences, Event handling | Sherylyn |
| CRED Tasks | Creating, Reading, Editing, Deleting of  tasks | Database, Event handling, Recycler view | Xin Le |x
| Calendar View | Displaying tasks in another format | Recycler view, Event handling | Kiara |
| Push Notifications | Send notifications when nearing the tasks' deadline | Push notifications | Alethea |
| Login/Signup | Allows users to login with an existing account or signup with a new account | Database, Shared preferences, Event handling | Gwyneth |


### Stage 2

| Feature | Description | In-charge |
| :------ | :---------- | :-------- | 
| Splash Screen | Opening Page of the App | Alethea |
| Random Quote | Shows a random quote on the splash screen to motivate users | Gwyneth |
| Background Music | Music to set the mood; Users can choose to opt in/out whenever | Alethea |
| Mood Statistics | Displays charts based on the mood selected in homepage | Xin Le |
| Total Productivity Time | Displays total study hours | Sherylyn |
| Firebase | Stores and sync data between users using cloud-hosted NoSQL database | Kiara |
| Widget | Lets users see the tasks they have due on that day and the timing it is due at | Gwyneth | 
| Punching Bag | Upgrade to the Destress Page; Lets user relieve stress by tapping on the picture they selected or the default punching bag, stimulating punching | Gwyneth | 
| Timer / Stopwatch | Allow user to set the duration of productiveness / Track how long the user has been productive  | Sherylyn |
| Edit/Delete User Profile | User can choose to edit the details in their profile and/or delete their account | Kiara |
| Guest Account | User can choose to use the app under a guest user | Kiara |


### Splash Page
* The opening page of the app
* Progress bar to show how long it takes for the app to load
* Shows a random quote to motivate users

![splash page](https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116245094/8ef47a2b-01a8-4813-9455-0b6f416febf8)

### Log In Page
* User keys in their account details and log into the app
* If the username or password is incorrect, they cannot log in
* Select "Remember Me" to stay logged in the app even when they close the app
* Select "Sign Up" if they want to create a new account
* "LOGIN" button brings the user to the home page if the account is valid

![login page](https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116245094/7b2278de-ffdf-44df-960c-be0efb6bbb04)

### Sign Up & Guest Account Page
* Username that user enters cannot exist in the database, must be unique
* Input of Password and Confirm Password must be the same
* Username and Password is case sensitive
* Users can also choose to use the app features using a guest account 

![signup page 2](https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116245094/04f654c5-f349-4300-af79-6eb48b7d343d)


### Home Page
* Users can choose how they are feeling 
* Shows tasks that are due today
* Can change the task from "Pending" to "Done" (will be updated in Task Page and Calendar)

![home page](https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116245094/6b8989c9-a2dd-473a-bf09-d6278c7b4ed1)

### Task
* Allows user to view all task
* Can create by pressing "+"
* Swipe right to edit, left to delete
* Can change the task from "Pending" to "Done" (will be updated in Home Page and Calendar)

![task page](https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116245094/f7fddedb-8e20-40f3-ac25-00cc86585655)

#### Create Task
* Enter details of the new task
* Task will be added to the task page, home page, calendar page and widget

<img width="195" alt="Screenshot 2023-07-31 014015" src="https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116245094/a89759a6-a507-449e-a173-64f17fd62420">

#### Edit Task
* Enter details to update task
* Edited task will be reflected on the task page, home page, calendar page and widget

![edit dialog](https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116245094/a3c12801-2770-4020-a4d9-5dae7a87c3b6)

#### Delete Task
* Prompt to user to make sure that they want to delete the task
* Deleted task will be removed from the task page, home page, calendar page and widget

### Calendar
* Alternative view from the task page 
* Click arrow buttons to change months
* Click on a date to see tasks for that date
* Can change the task from "Pending" to "Done" (will update in Home Page and Task Page)

![calendar](https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116245094/b5261fa9-fe43-4623-939c-09b7f149cc12)

### Timer
* Start and Reset buttons disabled if no task is selected
* Select task and timer duration is the task duration, circle fills up as time passes
* Start button becomes Pause when timer is running, and when Paused, Timer pauses and button changes to Resume
* Timer displays duration, then counts up instead of counting down so it is not as stressful

![timer page](https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116245094/bdedb947-7de3-4db9-a4e6-e98f589600ca)
![select task timer page](https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116245094/359c8b14-715d-4bac-a153-58a754f84ab2)
![task complete timer page](https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116245094/fe3f9d8b-7b73-4a02-b22f-3362d22fa7e1)


### Destress Page
* Select between Photos, Videos and Punching Bag for destressing

![destress page](https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116245094/a71a2100-9166-4d31-a88f-7d096d9ba377)
* Users have five minute to destress before being prompted to start work again

![destress prompt](https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116245094/611fffb0-e950-473b-91fa-f7ade9b1f4f7)

### Photos
* Funny photos will be displayed for the user to look at
* Scrollable
  
![photos](https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116245094/eee2ac14-489f-4e55-b97c-3a09944d624e)

### Videos
* Different relaxing videos will play
* Can choose video to watch

![video](https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116245094/d4733e2e-4b59-4c42-8d28-a36dbeb5663e)

### Punching Bag 
* Tap on the screen to stimulate punching

![punching bag](https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116245094/312c6e46-818e-4c8f-b17d-53b23f05ae80)

* Users can select their desired picture to 'punch'

![punching bag2](https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116245094/25356c51-1788-45f8-9e1f-de5b32370f28)

* 'Hope you are doing well' prompt will appear after users tap on the screen for more than 50 times

![punching bag prompt](https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116245094/87481fb5-9f8c-48be-b278-86e4a8ebfcb0)

### Widget
* Users can view the number of tasks, the tasks name and due timing of the tasks due today
* Widget updates when there is a creation, edition or deletion of tasks
* Upon pressing the widget, it will prompt the user into the home page of the app

![widget](https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116245094/f7a754bc-46e9-42e6-b454-ccd57416efbd)

### Statistics (Mood and Total Study Time)
* Shows the amount of time spent on being productive, takes the total duration of all tasks that are completed
* Shows the mood of user throughout the week to be more aware of their emotional patterns, which can be integrated into goal-setting exercises

![stats page](https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116245094/a5d18ca8-732f-44de-8df8-7baf903fa414)

### Settings
* User can choose to edit profile, play/pause backgound music or log out of app

![home page with settings](https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116245094/05453946-492a-43f1-91fd-1776d40608ea)

#### Edit Profile
* User can change their profile details

![edit profile](https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116245094/0b9b0b8a-5922-49ab-8974-8dcaa2c7c5a8)

#### Delete Profile 
* Users can choose to delete their account
  
![delete profile](https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116245094/3b12612c-59ac-44b8-95a8-8fa3c1ea9ac9)
