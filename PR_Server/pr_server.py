import socket
import os
import sqlite3

"""
What should I do?
	- I should let users log in using credientials that are stored locally
	- I should receieve plain text PR and store the info in a file
	- I should be able to let users query their previous PRs
	- I should let admins change the approval status
	- I can be queried by the clients asking for approval status for pending PRs

Command List:
	- LOGIN <username> <password> (Return messages SUCCESS or FAILURE)
	- SENDPRDATA <ID> (then send data <DATA>)
	- GETPR <ID> (If ID = * then get all)
	- APPROVE <ID>
	- REJECT <ID>
	- EXIT
"""

S = socket.socket()
S.bind(("192.168.0.37", 5000))
S.listen(1)
EOPR = bytes(chr(4).encode())

class User():

	def __init__(self, username, user_group, userID, employeeID, ipaddress, conn):
		self._username = username
		self._group = user_group
		self._ipaddress = ipaddress
		self._employeeID = employeeID
		self.conn = conn

	def get_username(self):
		return self._username

	def get_group(self):
		return self._group

	def get_ipaddress(self):
		return self._ipaddress

class Perchase_Requisition():

	def __init__(self, ID):
		self._id = ID
		self._database_file = "PRUsers.db"
		self._PR_data = [self._id]

	def __str__(self):
		return "\n".join(self._PR_data)

	def get_id(self):
		return self._id

	def get_pr_data(self):
		return self._PR_data

	def get_database_file(self):
		return self._database_file

	def add(self, data):
		self._PR_data.append(data)

	def approve(self):
		pr = sqlite3.connect(self._database_file)
		c = pr.cursor()
		c.execute("""UPDATE Perchase_Requisition
					SET Status = "APPROVED"
					WHERE ID = {}""".format(self._id))
		pr.commit()
		pr.close()
		self._PR_data[12] = "APPROVED"

	def reject(self):
		pr = sqlite3.connect(self._database_file)
		c = pr.cursor()
		c.execute("""UPDATE Perchase_Requisition
					SET Status = "REJECT"
					WHERE ID = {}""".format(self._id))
		pr.commit()
		pr.close()
		self._PR_data[12] = "REJECT"

	def save_to_database(self):
		pr = sqlite3.connect(self._database_file)
		c = pr.cursor()
		c.execute("""INSERT INTO Perchase_Requisition (ID, UserNames, Description, EmployeeNumber, Department, Date_Of_Creation, Item_Name, Unit, Quantity, Price_Per_Unit, Total_Price, Vendor, Status)
					VALUES("{}", "{}", "{}", "{}", "{}", "{}", "{}", "{}", {}, {}, {}, "{}", "{}");""".format(
										self._PR_data[0],
										self._PR_data[1],
										self._PR_data[2], 
										self._PR_data[3],
										self._PR_data[4],
										self._PR_data[5],
										self._PR_data[6],
										self._PR_data[7],
										self._PR_data[8], 
										self._PR_data[9],
										self._PR_data[10],
										self._PR_data[11],
										self._PR_data[12]))
		pr.commit()
		pr.close()
	def load_from_database(ID):
		pr = sqlite3.connect(self._database_file)
		c = pr.cursor()
		c.execute("SELECT * FROM Perchase_Requisition WHERE ID = ?", (ID,))
		self._PR_data = list(c.fetchone)
		self._id = ID

def login(username, password, address, connection):
	users = sqlite3.connect('PRUsers.db')
	c = users.cursor()
	c.execute("SELECT * FROM Users WHERE UserName = ?", (username,))
	user = c.fetchone()
	if user[1] == password:
		return User(username, user[1], user[2], user[3], address[0], connection)

def send_pr(perchase_requisition, user):
	for info in perchase_requisition:
		user.conn.send(bytes("{}\n".format(info).encode()))
	user.conn.send(EOPR)

def cmd_mode(user):
	while True:
		msg = user.conn.recv(1024).decode("utf-8").split(" ")
		print(msg)
		if msg[0] == "":
			break
		if msg[0] == "LOGIN":
			user.conn.send(b"Already Logged in\n")
		elif msg[0] == "SENDPRDATA":
			pr = Perchase_Requisition(msg[1])
			received = False
			user.conn.send(b"READY\n")
			while not received:
				cat_str = ""
				print(pr.get_pr_data())
				while True:
					data = user.conn.recv(1).decode("utf-8")
					print(data.encode("utf-8"))
					if data == "\n":
						break
					if bytes(data.encode("utf-8")) == EOPR:
						print("FINISHED")
						received = True
						user.conn.send(b"RECEIVED\n")
						break
					cat_str = cat_str + data
				if not received:
					pr.add(cat_str)
			pr.save_to_database()
		elif msg[0] == "EXIT":
			break

		elif msg[0] == "GETPR":
			if msg[1] == "*":
				pr_database = sqlite3.connect(Perchase_Requisition(0).get_database_file())	
				c = pr_database.cursor()
				c.execute("SELECT * FROM Perchase_Requisition WHERE UserNames = ?", 
				(user.get_username(),))
				for pr in c.fetchall():
					for data in pr:
						print(data)
						user.conn.send(bytes("{}\n".format(data).encode()))
					user.conn.send(bytes("{}\n".format(chr(4)).encode()))
			else:
				pr = Perchase_Requisition(0).load_from_database(msg[1])
				send_pr(pr, user)
		elif msg[0] == "APPROVE":
			pr = Perchase_Requisition(0).load_from_database(msg[1])
			if int(self._PR_data[10]) <= 1000:
				if int(user.get_group()) <= 5:
					pr.approve()
			elif int(self._PR_data[10]) <= 10000:
				if int(user.get_group()) <= 3:
					pr.approve()
			else:
				if int(user.get_group()) <= 2:
					pr.approve()
		elif msg[0] == "REJECT":
			pr = Perchase_Requisition(0).load_from_database(msg[1])
			if int(self._PR_data[10]) <= 1000:
				if int(user.get_group()) <= 5:
					pr.reject()
			elif int(self._PR_data[10]) <= 10000:
				if int(user.get_group()) <= 3:
					pr.reject()
			else:
				if int(user.get_group()) <= 2:
					pr.reject()

def main():
	while True:
		conn, addr = S.accept()
		print(addr)
		msg = conn.recv(1024).decode("utf-8").split(" ")
		print(msg)
		if msg[0] == "LOGIN":
			print(msg[1], msg[2])
			user = login(msg[1], msg[2], addr, conn)
			if not user:
				print("Login failed")
				conn.send(b"FAILURE\n")
				conn.shutdown()
			else:
				print('SUCCESS')
				conn.send(b"SUCCESS\n")
				cmd_mode(user)
		

main()
