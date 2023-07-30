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
| Background Music | Can opt in or out of such music | Alethea |
| Statistics | Displays charts (mood/study hours) | Xin Le/Sheryl |
| Firebase | Stores and sync data between users using cloud-hosted NoSQL database | Kiara |
| Widget | Lets users see the tasks they have due on that day and the timing it is due at | Gwyneth | 
| Punching Bag | Upgrade to the Destress Page; Lets user relieve stress by tapping on the picture they selected or the default punching bag, stimulating punching | Gwyneth | 
| Timer / Stopwatch | Allow user to set the duration of productiveness / Track how long the user has been productive  | Sheryl |
| Task Sorting | Sort them into user-defined categories | - |
| Prompt to destress page | After each timer/stopwatch session, if the user has been productive for at least 1 hour, they will be prompted to visit the destress page to relax and unwind | - |
| Exit Prompt | When trying to exit the app while the timer/stopwatch is running | - |



### Splash Page
* The opening page of the app
* Progress bar to show how long it takes for the app to load
* Shows a random quote to motivate users

### Log In Page
* User keys in their account details and log into the app
* If the username or password is incorrect, they cannot log in
* Select "Remember Me" to stay logged in the app even when they close the app
* Select "Sign Up" if they want to create a new account
* "LOGIN" button brings the user to the home page if the account is valid

![ss1](https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116244909/ab95ef04-3b40-4ec3-b964-8d7e15096c73)

### Sign Up Page
* Username that user enters cannot exist in the database, must be unique
* Input of Password and Confirm Password must be the same
* Username and Password is case sensitive

![ss8](https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116244909/f016f9a7-5cb4-46b2-b67b-a5ef9862461d)

### Home Page
* Users can choose how they are feeling 
* Shows tasks that are due today
* Can change the task from "Pending" to "Done" (will be updated in Task Page and Calendar)
 
![ss2](https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116244909/0a44661e-811a-42a0-a1e3-2babcb96e596)

### Task
* Allows user to view all task
* Can create by pressing "+"
* Swipe right to edit, left to delete
* Can change the task from "Pending" to "Done" (will be updated in Home Page and Calendar)

![ss3](https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116244909/807a6dc2-0cff-4bfd-a5ed-96bbe6e58854)

### Create Task
* Enter details of the new task
* Task will be added to the task page, home page, calendar page and widget

![ss9](https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116244909/15f9e982-e6f0-4a7e-ae55-a2f34c88ac26)

### Edit Task
* Enter details to update task
* Edited task will be reflected on the task page, home page, calendar page and widget
  
### Delete Task
* Prompt to user to make sure that they want to delete the task
* Deleted task will be removed from the task page, home page, calendar page and widget

### Calendar
* Alternative view from the task page 
* Click arrow buttons to change months
* Click on a date to see tasks for that date
* Can change the task from "Pending" to "Done" (will update in Home Page and Task Page)

![ss4](https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116244909/b2f00a5a-9f8a-4c9e-880c-72e7d5ea83da)

### Timer
*
*
*

### Destress Page
* Select between Photos, Videos and Punching Bag for destressing
* Users have one minute to destress before being prompted to start work again
   
![ss5](https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116244909/e86b2428-3d30-4fc8-9597-5365dd0014b4)

### Photos
* Funny photos will be displayed for the user to look at
* Scrollable
  
![ss6](https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116244909/0520d2b9-3fa4-4eac-bb56-bca4adf1b19e)

### Videos
* Different relaxing videos will play
* Can choose video to watch
  
![ss7](https://github.com/MAD2023T05-Team2/MAD_ASG/assets/116244909/899cd9a1-3b3a-4363-a93f-f680c933a556)

### Punching Bag 
* Tap on the screen to stimulate punching
* Users can select their desired picture to 'punch'
* 'Hope you are doing well' prompt will appear after users tap on the screen for more than 50 times

### Widget
* Users can view the number of tasks, the tasks name and due timing of the tasks due today
* Widget updates when there is a creation, edition or deletion of tasks
* Upon pressing the widget, it will prompt the user into the home page of the app

### Statistics 
* Shows the amount of time spent on being productive
* Shows the mood of user throughout the week to be more aware of their emotional patterns, which can be integrated into goal-setting exercises

