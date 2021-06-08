let accDisplay = false;

//User variables
let depositWindow = document.querySelector("#depositWindow");
let withdrawWindow = document.querySelector("#withdrawWindow");
let transferWindow = document.querySelector("#transferWindow");
let	openAccWindow = document.querySelector("#openAccWindow");
let	jointAccWindow = document.querySelector("#jointAccWindow");
let	closeAccWindow = document.querySelector("#closeAccWindow");

let userTable = document.querySelector("#userTable");

let myLocalUser = JSON.parse(sessionStorage.getItem("myUser"));

let currentUser = {
    // firstName: "",
    // id: 0,
    // lastName: "",
    // password: "",
    // userStatus: 0,
    // username: ""
}

//Admin variables
let adminTable = document.querySelector("#adminTable");
let	approveUserWindow = document.querySelector("#approveUserWindow");
 
let allUsersDiv = document.querySelector("#allUsersDiv");


let	customBackground = document.querySelector(".customBackground");


function showLoginMenu() {
    // location = "user.html";
    var menu = document.querySelector(".menu");
    var form = document.querySelector(".loginForm");

    menu.className = "hide";
    form.className = "loginForm start";
}


function validateUser() {

    currentUser.username = document.querySelector("#username1").value;
    currentUser.password = document.querySelector("#password1").value;

    if (currentUser.username==="admin1" && currentUser.password==="pass1") {
        location = "admin.html";
    }
    else {

        let url = `http://localhost:8080/JerseyJDBCBank/api/controller/user/${currentUser.username}/${currentUser.password}`;

        let request = new XMLHttpRequest();

        request.onreadystatechange = function () {

            if (this.readyState == 4) {
                let response = JSON.parse(request.response);
                //console.log(response)

                if (response != null && response.userStatus === 0) {

                    // currentUser = response;

                    alert("Your application has to be approved.");
                    return;
                }

                if (response != null) {

                    currentUser = response;

                    alert("User verified!");
                    
                    sessionStorage.setItem("myUser", JSON.stringify(currentUser));

                    document.querySelector("#username1").value = "";
                    document.querySelector("#password1").value = "";

                    location = "user.html"
                }
                else if (response == null) {
                    alert("Invalid credentials!")
                }
                //console.log(currentUser);

            }
        }
        request.open("GET", url);

        request.send();

    }

}


function showUserAccounts() {
	if(accDisplay===false){

	let parsedUserAccounts;
	userTable.className = "start"
	
    let url = `http://localhost:8080/JerseyJDBCBank/api/controller/user/${myLocalUser.username}/${myLocalUser.password}/${myLocalUser.id}/acc`;
    let request = new XMLHttpRequest();
    request.onreadystatechange = function () {
        if (this.readyState == 4) {
				let headerRow = document.createElement("tr");
                // userTable.setAttribute('style', 'border-radius: 15px !important; background-color: white;')
                userTable.setAttribute('style', 'background-color: white;')
                headerRow.setAttribute('style', 'background-color: gray !important;')
                // headerRow.setAttribute('style', 'background-color: gray;')
				let header1 = document.createElement("th");
				let header2 = document.createElement("th");
				let header3 = document.createElement("th");
				
				header1.innerHTML="Account ID";
				header2.innerHTML="Type";
				header3.innerHTML="Balance";					
	
				headerRow.append(header1, header2, header3);
				userTable.append(headerRow)

                document.querySelector("#welcomeMsg").className ="hide";
                document.querySelector("#accSum").className ="start";

	            parsedUserAccounts = JSON.parse(request.response);
	            
	            sessionStorage.setItem("myAccounts", JSON.stringify(parsedUserAccounts));

            for (var i = 0; i < parsedUserAccounts.length; i++) {
				let row = document.createElement("tr");
				let data1 = document.createElement("td");
				let data2 = document.createElement("td");
				let data3 = document.createElement("td");
                data1.append(parsedUserAccounts[i].id);
                data2.append(parsedUserAccounts[i].type);
                data3.append("$" + parsedUserAccounts[i].balance.toFixed(2));
                row.append(data1, data2, data3);
                if(i%2===0){
                    row.setAttribute('style', 'background-color:  #becef3;'); 
                }
                userTable.append(row);
            }
            
            // userTable.setAttribute('style', 'border-radius: 15px !important; background-color: white;')
            // headerRow.setAttribute('style', 'background-color: gray !important;')
            // userTable.setAttribute('style', 'border-radius: 15px; background-color: white;')
            // headerRow.setAttribute('style', 'border-radius: 15px; background-color: gray;')
            
            //console.log(request.response);
        }
    }
    request.open("GET", url);
    request.send();
    }
    accDisplay = true;
}


function registerUser() {

    let user = {
        firstName: document.querySelector("#fNameReg").value,
        lastName: document.querySelector("#lNameReg").value,
        username: document.querySelector("#uNameReg").value,
        password: document.querySelector("#passReg").value
    }
    let repeatPass = document.querySelector("#rpassReg").value;

    if(user.password != repeatPass) {
        alert("Your passwords don't match. Please try again.");
        document.querySelector("#passReg").value="";
        document.querySelector("#rpassReg").value ="";
        return;
    }

    if(user.firstName ==="" ||user.lastName ==="" || user.username ==="" || user.password ==="" || repeatPass ==="") {
        alert("Your application is incomplete. Please fill in all the required fields.");
        return;
    }

    let jsonUser = JSON.stringify(user);

    let url = "http://localhost:8080/JerseyJDBCBank/api/controller/users";

    let request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (this.readyState == 4) {
            let responseParsed = request.response.toString();
            //console.log(responseParsed)
            if (responseParsed === "true"){
                alert("Your registration has been submitted!");
                document.querySelector("#fNameReg").value ="";
                document.querySelector("#lNameReg").value="";
                document.querySelector("#uNameReg").value="";
                document.querySelector("#passReg").value="";
                document.querySelector("#rpassReg").value ="";
            }
            else if (responseParsed === "false"){
                alert("This username is not available. Please choose a different one in order to register.");
                document.querySelector("#uNameReg").value="";
                document.querySelector("#passReg").value="";
                document.querySelector("#rpassReg").value ="";
                
            }


            // document.querySelector("#fNameReg").value ="";
            // document.querySelector("#lNameReg").value="";
            // document.querySelector("#uNameReg").value="";
            // document.querySelector("#passReg").value="";
            // document.querySelector("#rpassReg").value ="";
        }
    }

    request.open("POST", url);
    request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    request.send(jsonUser);

}


function openAccount() {
    let id = myLocalUser.id
    let accType;

    if (document.querySelector("#savingsAcc").checked === true) {
        accType = document.querySelector("#savingsAcc").value
        //console.log(accType);
    }
    else if (document.querySelector("#checkingAcc").checked === true) {
        accType = document.querySelector("#checkingAcc").value
        //console.log(accType);
    }

    //console.log(id);

    let url = `http://localhost:8080/JerseyJDBCBank/api/controller/users/${id}/${accType}`;

    let request = new XMLHttpRequest();
    
            request.onreadystatechange = function () {
            if (this.readyState == 4) {
				accDisplay=false;
				userTable.innerHTML="";
				document.querySelector("#savingsAcc").checked = false;
				document.querySelector("#checkingAcc").checked = false;
				showUserAccounts();
				alert("Account has been opened!")
            }
        }

    request.open("POST", url);
    // request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    request.send();

}


function deleteAccount() {
    let accId = document.querySelector("#accDel").value;
    let myLocalAccounts = JSON.parse(sessionStorage.getItem("myAccounts"));
    let count = false;

    let url = `http://localhost:8080/JerseyJDBCBank/api/controller/users/${accId}`;

    		for(var i = 0; i < myLocalAccounts.length; i++) {
			if(myLocalAccounts[i].id == accId && myLocalAccounts[i].balance > 0){
				alert("This account cannot be closed as the balance of $" + myLocalAccounts[i].balance + " is more than $0.00.");
				count = true;
				return;
			}
			else if(myLocalAccounts[i].id == accId && myLocalAccounts[i].balance == 0){
				count = true;
				let request = new XMLHttpRequest();
				
				    request.onreadystatechange = function () {
				        if (this.readyState == 4) {
							accDisplay=false;
							userTable.innerHTML="";
							
							showUserAccounts();
								alert("Account has been closed!")
				            //console.log(request.response);
				        }
				    }				
				    request.open("DELETE", url);
				    request.send();
			}
		}
		document.querySelector("#accDel").value ="";
		if(count === false){
			alert("Please enter a valid account number.")
		} 

}


function depositAccount() {

    let accMoney = document.querySelector("#accMoney").value;
    let accNum = document.querySelector("#accNum").value;
    

    

    if (accMoney <= 0) {
        //document.querySelector("#accNum").value ="";
        //document.querySelector("#accMoney").value ="";
        alert("Please enter a positive number.");
       //return;
    }

    if (isNaN(accNum)) {
        //document.querySelector("#accNum").value ="";
        //document.querySelector("#accMoney").value ="";
        alert("Please enter a valid account number.");
        return;
    }


        if(accMoney > 0) {

        let url = `http://localhost:8080/JerseyJDBCBank/api/controller/users/${accMoney}/${accNum}`;

        let request = new XMLHttpRequest();

        request.onreadystatechange = function () {
            if (this.readyState == 4) {
				accDisplay=false;
				userTable.innerHTML="";
				document.querySelector("#accNum").value ="";
				document.querySelector("#accMoney").value ="";

				showUserAccounts();
                //console.log(request.response);                
                alert("Deposit has been completed!")
            }
        }
        request.open("PUT", url);
        request.send();
    } 

}


function withdrawAccount() {
    let accMoney = document.querySelector("#accWithdrawMoney").value;
    let accNum = document.querySelector("#accWithdrawNum").value;

    if (accMoney <= 0 || isNaN(accMoney)) {
        alert("Please enter a positive number.");
    }

    else {

        let url = `http://localhost:8080/JerseyJDBCBank/api/controller/users/acc/${accMoney}/${accNum}`;

        let request = new XMLHttpRequest();

        request.onreadystatechange = function () {
            if (this.readyState == 4) {
				accDisplay=false;
				userTable.innerHTML="";
				document.querySelector("#accWithdrawMoney").value ="";
				document.querySelector("#accWithdrawNum").value ="";
				showUserAccounts();
				alert("Withdrawal has been completed!")
                //console.log("Funds withdrawn!" + request.response);
            }
        }
        request.open("PUT", url);
        request.send();
    }

}

function transferFunds() {
    let accFromNum = document.querySelector("#accFromNum").value;
    let accToNum = document.querySelector("#accToNum").value;
    let accTransMoney = document.querySelector("#accTransMoney").value;

    if (accTransMoney <= 0 || isNaN(accTransMoney)) {
        alert("Please enter a positive number.");
    }

    else {

        let url = `http://localhost:8080/JerseyJDBCBank/api/controller/users/transfer/${accTransMoney}/${accFromNum}/${accToNum}`;

        let request = new XMLHttpRequest();

        request.onreadystatechange = function () {
            if (this.readyState == 4) {
				accDisplay=false;
				userTable.innerHTML="";
				document.querySelector("#accFromNum").value ="";
				document.querySelector("#accToNum").value ="";
				document.querySelector("#accTransMoney").value ="";
				showUserAccounts();
				alert("Transfer has been completed!")
                //console.log("Funds transferred!" + request.response);
            }
        }
        request.open("PUT", url);
        request.send();
    }

}


function openJointAccount() {

    let jointAccNum = document.querySelector("#jointAccNum").value;
    let jointUsername = document.querySelector("#jointUsername").value;

    let url = `http://localhost:8080/JerseyJDBCBank/api/controller/users/joint/${jointUsername}/${jointAccNum}`;

    let request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (this.readyState == 4) {
				accDisplay=false;
				userTable.innerHTML="";
				document.querySelector("#jointAccNum").value ="";
				document.querySelector("#jointUsername").value ="";
				document.querySelector("#jointFName").value ="";
				document.querySelector("#jointLName").value ="";
				showUserAccounts();
				alert("Joint account has been opened!")
        }
    }
    request.open("POST", url);
    request.send();

}


// ADMIN FUNCTIONS
function showAllUsers() {
	document.querySelector("#welcomeMsg").className ="hide";
    
	approveUserWindow.className = "hide";
	adminTable.innerHTML="";
	adminTable.className = "start";

    document.querySelector("#accountsHead").className ="hide";
    document.querySelector("#usersAndAccountsHead").className ="hide";
    document.querySelector("#usersHead").className ="start";
	
	let parsedUsers;

    let url = "http://localhost:8080/JerseyJDBCBank/api/controller/users";

    let request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (this.readyState == 4) {
	
				let headerRow = document.createElement("tr");
				let header1 = document.createElement("th");
				let header2 = document.createElement("th");
				let header3 = document.createElement("th");
				let header4 = document.createElement("th");
				let header5 = document.createElement("th");
				let header6 = document.createElement("th");
				let header7 = document.createElement("th");
				
				header1.innerHTML="#";
				header2.innerHTML="First Name";
				header3.innerHTML="Last Name";
				header4.innerHTML="Username";
				header5.innerHTML="User ID";
				header6.innerHTML="Password";
				header7.innerHTML="Status";				
	
				headerRow.append(header1, header2, header3, header4, header5, header6, header7);
                headerRow.setAttribute('style', 'background-color: gray !important;')
				adminTable.append(headerRow);

            parsedUsers = JSON.parse(request.response)
            .sort(function(a, b) {
			var nameA = a.lastName.toUpperCase();
			var nameB = b.lastName.toUpperCase();
			if (nameA < nameB) {
			return -1;
			}
			if (nameA > nameB) {
			return 1;
			}
			return 0;
			});
							            

            for (var i = 0; i < parsedUsers.length; i++) {
               	let row = document.createElement("tr");
				let data1 = document.createElement("td");
				let data2 = document.createElement("td");
				let data3 = document.createElement("td");
				let data4 = document.createElement("td");
				let data5 = document.createElement("td");
				let data6 = document.createElement("td");
				let data7 = document.createElement("td");
                data1.append(i+1);
                data2.append(parsedUsers[i].firstName);
                data3.append(parsedUsers[i].lastName);
                data4.append(parsedUsers[i].username);
                data5.append(parsedUsers[i].id);
                data6.append(parsedUsers[i].password);
                
                if(parsedUsers[i].userStatus === 0){
					//data7.setAttribute('style', 'color: red !important');
					data7.append("Pending");
					data7.setAttribute('style', 'color: red !important');
				} else {
					data7.append("Approved");
                    data7.setAttribute('style', 'color: green !important');
				}
                
                if(i%2===0){
                    row.setAttribute('style', 'background-color:  #becef3;'); 
                }
                row.append(data1, data2, data3, data4, data5, data6, data7);
                adminTable.append(row);

            }
            //console.log(request.response);
        }
    }
    request.open("GET", url);
    request.send();
}

function showAllAccounts() {
	
	approveUserWindow.className = "hide";
    
	adminTable.innerHTML="";
	adminTable.className = "start";
    document.querySelector("#usersHead").className ="hide";
    document.querySelector("#usersAndAccountsHead").className ="hide";
    document.querySelector("#accountsHead").className ="start";

	let parsedAllAccounts;

    let url = "http://localhost:8080/JerseyJDBCBank/api/controller/accounts";

    let request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (this.readyState == 4) {
				let headerRow = document.createElement("tr");
				let header1 = document.createElement("th");
				let header2 = document.createElement("th");
				let header3 = document.createElement("th");
				let header4 = document.createElement("th");
				
				header1.innerHTML="#";
				header2.innerHTML="Type";
				header3.innerHTML="Account ID";
				header4.innerHTML="Balance";						
	
				headerRow.append(header1, header2, header3, header4);
                headerRow.setAttribute('style', 'background-color: gray !important;')
				adminTable.append(headerRow);

	            parsedAllAccounts = JSON.parse(request.response)
	            .sort(function(a, b) {
				var nameA = a.type;
				var nameB = b.type;
				if (nameA < nameB) {
				return -1;
				}
				if (nameA > nameB) {
				return 1;
				}
				return 0;
				});
	            

            for (var i = 0; i < parsedAllAccounts.length; i++) {
				let row = document.createElement("tr");
				let data1 = document.createElement("td");
				let data2 = document.createElement("td");
				let data3 = document.createElement("td");
				let data4 = document.createElement("td");
                data1.append(i+1);
                data2.append(parsedAllAccounts[i].type);
                data3.append(parsedAllAccounts[i].id);
                data4.append("$" + parsedAllAccounts[i].balance.toFixed(2));
                row.append(data1, data2, data3, data4);

                if(i%2===0){
                    row.setAttribute('style', 'background-color:  #becef3;'); 
                }
                adminTable.append(row);
            }
            
            //console.log(request.response);
        }
        }
    

    request.open("GET", url);
    request.send();
}



function showUsersAndAccounts() {



	allUsersDiv.innerHTML="";
	adminTable.innerHTML="";
    document.querySelector("#usersHead").className ="hide";
    document.querySelector("#accountsHead").className ="hide";
    document.querySelector("#usersAndAccountsHead").className ="start";
	
	approveUserWindow.className = "hide";
	allUsersDiv.className = "start"
	adminTable.className = "start"

    let parsed;
    let url = "http://localhost:8080/JerseyJDBCBank/api/controller/usersandaccounts";
    let request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (this.readyState == 4) {
	
				let headerRow = document.createElement("tr");
				let header1 = document.createElement("th");
				let header2 = document.createElement("th");
				let header3 = document.createElement("th");
				let header4 = document.createElement("th");
				let header5 = document.createElement("th");
				let header6 = document.createElement("th");
				let header7 = document.createElement("th");
				let header8 = document.createElement("th");
				
				header1.innerHTML="#";
				header2.innerHTML="User ID";
				header3.innerHTML="Username";
				header4.innerHTML="First Name";
				header5.innerHTML="Last Name";
				header6.innerHTML="Account Type";
				header7.innerHTML="Account ID";	
				header8.innerHTML="Balance";				
	
				headerRow.append(header1, header2, header3, header4, header5, header6, header7, header8);
                headerRow.setAttribute('style', 'background-color: gray !important;')
				adminTable.append(headerRow);

           parsed = JSON.parse(request.response).sort();	
				
           for (var i = 0; i < parsed.length; i++) {
	
				let stringArray = parsed[i].split(" ");
				stringArray.unshift(i+1);
				let row = document.createElement("tr");
				
				for (var j = 0; j < stringArray.length; j++){
					let data = document.createElement("td");
					data.append(stringArray[j]);

                    if(i%2===0){
                        row.setAttribute('style', 'background-color:  #becef3;'); 
                    }
					row.append(data);
				}

                adminTable.append(row);
}
            
        }
    }

    request.open("GET", url);
    request.send();
}



function approveUser() {
	
	    let userId = document.querySelector("#approveUserId").value;

    if (isNaN(userId)) {
        alert("Please enter a valid user ID.");
    }
    else {

        let url = `http://localhost:8080/JerseyJDBCBank/api/controller/approve/${userId}`;

        let request = new XMLHttpRequest();

        request.onreadystatechange = function () {
            if (this.readyState == 4) {
	
				
				document.querySelector("#approveUserId").value ="";

				showAllUsers();
                //console.log(request.response);                
                alert(`User with ID #${userId} has been approved!`)
                adminTable.innerHTML="";
            }
        }
        request.open("PUT", url);
        request.send();
    } 
	
}



/*function myFunc() {
    let myLocalUser = JSON.parse(sessionStorage.getItem("myUser"));
    let myLocalAccounts = JSON.parse(sessionStorage.getItem("myAccounts"));
    console.log("1: ", myLocalUser);
    console.log("2: ", myLocalAccounts);
    
    		for(var i = 0; i < myLocalAccounts.length; i++) {
			if(myLocalAccounts[i].id === 27){
				console.log("Id:", myLocalAccounts[i].id, myLocalAccounts[i].balance)
			}
		}
}*/

// Dynamic User windows - show and hide on click
function showDepositWindow() {
	closeAccWindow.className = "hide";
	jointAccWindow.className = "hide";
	openAccWindow.className = "hide";
	transferWindow.className = "hide";
	withdrawWindow.className = "hide";
	depositWindow.className = "start";
}

function showWithdrawWindow() {
	closeAccWindow.className = "hide";
	jointAccWindow.className = "hide";
	openAccWindow.className = "hide";
	transferWindow.className = "hide";
	depositWindow.className = "hide";
	withdrawWindow.className = "start";
}

function showTransferWindow() {
	closeAccWindow.className = "hide";
	jointAccWindow.className = "hide";
	openAccWindow.className = "hide";
	depositWindow.className = "hide";
	withdrawWindow.className = "hide";
	transferWindow.className = "start";
}

function showOpenAccWindow() {
	closeAccWindow.className = "hide";
	jointAccWindow.className = "hide";
	depositWindow.className = "hide";
	withdrawWindow.className = "hide";
	transferWindow.className = "hide";
	openAccWindow.className = "start";
}


function showJointAccWindow(){
	closeAccWindow.className = "hide";
	depositWindow.className = "hide";
	withdrawWindow.className = "hide";
	transferWindow.className = "hide";
	openAccWindow.className = "hide";
	jointAccWindow.className = "start";
}

function showCloseAccWindow(){
	depositWindow.className = "hide";
	withdrawWindow.className = "hide";
	transferWindow.className = "hide";
	openAccWindow.className = "hide";
	jointAccWindow.className = "hide";
	closeAccWindow.className = "start";
}


//Dynamic Admin windows

function showApproveUser(){
	approveUserWindow.className = "start";
}



function myCustomBackground(){
    setTimeout(myBk, 6000)
}

function myBk() {
    customBackground.className = "hide";
    document.querySelector("body").setAttribute('style', 'background-color:  #6f88a7;');  
}

function showMyName(){

    document.querySelector(".greetUser").innerHTML = `Hi, ${myLocalUser.firstName}!`;
}