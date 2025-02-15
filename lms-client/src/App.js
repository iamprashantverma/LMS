import React from 'react';
import NavBar from './components/Header/NavBar';
import Footer from './components/Footer/Footer';
import PublicRoutes from './routes/PublicRoutes';
import './App.css';


function App() {
  return (

        <div className="App">
          <div className="navbar">
            <NavBar />
          </div>

          <div className="content">
            <PublicRoutes/> 
          </div>

          {/* <div className="footer">
            <Footer />
          </div> */}
        </div>
  );
}

export default App;
