Use the following rules to evaluate and validate the vehicle details.
If there are rule failures, then create a response with rule failures or validation failures.
If there are multiple rule failures, then add all rule failures in the response.

Vehicle Validation Rules:
1) If the vehicle has more than 9 seats, then add a rule failure with code '002_1' and message 'Cannot have more than 9 seats'
2) If the vehicle length is more than 600 centimeters, then add a rule failure with code '003_1' and message 'Cannot be longer than 600 centimeters'

Vehicle Details:
{vehicleDetails}

Response json format:
{format}