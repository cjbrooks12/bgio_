# witch-hunt

A local server for playing [Witch Hunt](https://www.level99games.com/witch-hunt) game.

## What is this?

This is an application to aid in playing the Witch Hunt party game. It was created because the moderator app provided by the game's creators is pretty bad, and will not save a game's data if the webpage is accidentally refreshed. In addition, for such a large game, it is a bit difficult to communicate the necessary information, so this app will help facilitate communication between the moderator and the other players.

This application consists of the following components:

- a Ktor websocket server
- a Kotlin/JS websocket game client
- common code shared between both the server and the client
- a set of shell scripts to start serving the app on localhost, and open it up for public connections through an SSH tunnel using Serveo
- A page with a QR code players can scan to link directly to the SSH tunnel's public URL

Note that while users connect to Serveo through an encrypted tunnel over HTTPS, the app itself has no security mechanisms built-in. It is recommended that you change the subdomain you use to something private and unique, so that no one else can connect to your server while you're playing.

## How to use?

Clone the project, and run `./start.sh` to start running the app in the background and serve it through a Serveo SSH tunnel. `./stop.sh` will close that tunnel and stop the local server. To run locally without opening up the SSH tunnel, use `./gradlew run`, which will also run in the foreground and may be stopped with ctrl+c.

## Architecture

This app is built as a Ktor server, which will serve the initial webpage needed to connect to the websocket client. Once connects, the websocket server will push single messages or chunks of HTML to the client, which will then render them to the page appropriately. There isn't much interactivity done on the client itself, most updates are handled by the server pushing HTML to the client.

Incoming messages are JSON message objects, which get deserialized on the server with the kotlinx.serialization utilities. Likewise, outgoing messages are JSON messages, which are deserialized on the client to update the webpage.

On the server, the websocket is set up as a pair of Actors, which continually process the incoming messages in the order they are received. The "requests" actor is unbounded, so clients will not suspend while waiting for a request to be handled. Each action that may may need to push data to connected clients, such as users joining/leaving, or actions taken on the client, is a unique request object.

The game state is managed as an immutable State object. Since Request handlers are sequential and well-ordered, they can "mutate" the state by creating a copy of it and returning the new, modified state. Anytime the server detects a change in state, it will push UI updates to all connected clients, to ensure they are always in sync with the application state.

The "responses" actor is responsible for sending text messages and HTML fragments to connected clients. Data will only ever be sent to connected clients through this actor. This is a "bounded" actor, so the client sending messages out will suspend until these updates have finishing being sent to connected clients as appropriate. All updates may be transmitted in one of the following manners:

- Broadcast: One message is sent to all connected users. Each message is tailored specific for its recipient, and so the actual message sent to each user may be different.
- Response: The server responds to the user who made a request. The message is formatted specifically for the sender, and no one else will receive that response.
- Private Message: One user sends a message directly to another. The message will show up in the message list for both the sending and receiving users, and is formatted individually for each user.

