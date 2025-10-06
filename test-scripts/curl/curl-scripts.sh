# Register admin user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "adminuser1",
    "password": "AdminPassword",
    "role": "ROLE_ADMIN"
  }'

# Login admin user
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "adminuser1",
    "password": "AdminPassword"
  }' 


# Register emp user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "empuser1",
    "password": "EmpPassword",
    "role": "ROLE_USER"
  }'
  
# Login emp user
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "empuser1",
    "password": "EmpPassword"
  }'

# List All Employees  
curl -X GET http://localhost:8080/api/employees \
  -H "Authorization: Bearer your_token_here"

# Get Employee by ID  
curl -X GET http://localhost:8080/api/employees/1 \
  -H "Authorization: Bearer your_token_here"

# Create Employee  
curl -X POST http://localhost:8080/api/employees \
  -H "Authorization: Bearer your_token_here" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Srijit Paul",
    "designation": "Software Engineer",
    "salary": 85000
  }'

# Update Employee  
curl -X PUT http://localhost:8080/api/employees/11 \
  -H "Authorization: Bearer your_token_here" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Srijit Paul",
    "designation": "Senior Software Engineer",
    "salary": 95000,
	"email": "paul.srijit@company.com",
	"department": "Engineering"
  }'

# Delete Employee  
curl -X DELETE http://localhost:8080/api/employees/11 \
  -H "Authorization: Bearer your_token_here"