import { BrowserRouter, Routes, Route } from "react-router-dom";
import HomePage from "./pages/HomePage";
import DetailPage from "./pages/DetailPage";
import SearchPage from "./pages/SearchPage";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<HomePage />} />
                <Route path="/place/:id" element={<DetailPage />} />
                <Route path="/search" element={<SearchPage />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;