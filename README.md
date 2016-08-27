Spring Security has always been one of my favourites which I explore often, since the time of xml configuration.

This time I tried to implement Security with JWT token using Spring Security module (Java config) and I must say the java config has changed much since the time of XML configuration.

Let’s chalk out the high level flow first:

1) User at first will fetch the JWT token from the URL: <b>http://localhost:8080/SpringToken/auth</b>
by passing the user id and password in json format in the request body.

We will make this URL <b>“permit all”</b> in Spring Security so that Spring does not enforce any authentication or authorization rule for this URL and anonymous authentication is implemented with <b>AnonymousAuthenticationToken.</b>



This URL invokes a Handler method which generates JWT token sets it in the Security Context holder and returns it to the client.


2) The client while making subsequent request to access resources need to set the token in the request header named <b>X-Auth-Token.</b>


3) The JWT token will get expired after the expiration time (which we set during token generation), in this case we need to access the URL: <b>http://localhost:8080/SpringToken/auth/refresh,</b> which refreshes the token again.
In order to refresh a token, existing token needs to be supplied.



The code can be accessed at: https://github.com/MicSpring/SpringToken

Let’s analyze the flow from component point of view:

Here we have implemented a custom filter named <b>“AuthenticationTokenFilter”</b> and we have positioned it before <b>UsernamePasswordAuthenticationFilter</b> in the configuration.

This <b>“AuthenticationTokenFilter”</b> itself implements <b>UsernamePasswordAuthenticationFilter.</b>

At first when the user tries to access the URL <b>http://localhost:8080/SpringToken/auth</b> to fetch the token, this filter checks the request header for the token, since the token is not present, the control passes on to the next filter in the Spring Filter Chain, since we have configured anonymous authentication for this URL <b>(using "permitAll" in the configuration)</b> so the control reaches the handler method of the controller named <b>“AuthenticationController” (instead of giving ACCESS-DENIED 401).</b>

This controller validates the credentials (as present in the request body) with the help of the configured AuthenticationManager which uses AuthenticationProvider for the validation then autheticates it and generates authentication token. Then it generates JWT and provide it to the user in a response body.

Now if the user requests for a resource without the token in the request header then an Access Denied (401)exception is obtained.
if the user sets the token as obtained above in the request header and goes for the resource at <b>http://localhost:8080/SpringToken/resource/book</b> then our custom filter extracts the username from the  token, validates the token, creates authentication token sets it in the Spring SecurityContextHolder and passes the request to the next filter in the Spring Filter Chain for further processing ans lasty to the resource handler method.

Validation of the token includes whether the token is still alive.
Now if the token is expired, then we need to refresh the token with the URL <b> http://localhost:8080/SpringToken/auth/refresh </b> which is also exempted from Spring Security validation


