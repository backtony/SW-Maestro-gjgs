import logo from "./logo.svg";
import "./App.css";
import MainView from "./screens/main/MainView";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import LoginView from "./screens/login/LoginView";
import firebase from "firebase";
import LoginUserNameView from "./screens/login/LoginUserNameView";
import BackofficeLoginView from "./screens/login/BackofficeLoginView";
import BackofficeMainView from "./screens/backoffice/BackofficeMainView";
import SuperAdminLoginView from "./screens/login/SuperAdminLoginView";

function App(): any {
  if (!firebase.apps.length) {
    firebase.initializeApp({
      apiKey: "AIzaSyAFThY9ph4bl6vT0W0-E7WrN4YhT6LAgj8",
      authDomain: "gjgs-1057b.firebaseapp.com",
      projectId: "gjgs-1057b",
      storageBucket: "gjgs-1057b.appspot.com",
      messagingSenderId: "642887094981",
      appId: "1:642887094981:web:86acff6a094679ebb756ac",
      measurementId: "G-VN9RC3YQ48",
    });
  } else {
    firebase.app();
  }

  return (
    <Router>
      <Switch>
        <Route path="/react">
          <div className="App">
            <header className="App-header">
              <img src={logo} className="App-logo" alt="logo" />
              <p>
                Edit <code>src/App.tsx</code> and save to reload.
              </p>
              <a
                className="App-link"
                href="https://reactjs.org"
                target="_blank"
                rel="noopener noreferrer"
              >
                Learn React
              </a>
            </header>
          </div>
        </Route>
        <Route path="/backoffice/login">
          <BackofficeLoginView />
        </Route>
        <Route path="/backoffice">
          <BackofficeMainView path="backoffice" />
        </Route>
        <Route path="/login">
          <LoginView />
        </Route>
        <Route path="/login-username">
          <LoginUserNameView />
        </Route>
        <Route path="/login-super-admin">
          <SuperAdminLoginView />
        </Route>
        <Route path="/main">
          <MainView path="main" />
        </Route>
        <Route path="">
          <div className="cococo2">
            <div className="cococo">가지</div>
            <div className="cococo">각색</div>
            <div>
              <a href="/login">로그인페이지 보기</a>
            </div>
            <div>
              <a href="/main/home">메인페이지 보기</a>
            </div>
            <div>
              <a href="/backoffice/login">백오피스 로그인페이지 보기</a>
            </div>
          </div>
        </Route>
      </Switch>
    </Router>
  );
}

export default App;
