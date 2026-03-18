import { useState } from "react";
import Header from "../components/Header";
import Hero from "../components/Hero";
import RegionList from "../components/RegionList";
import PlaceCard from "../components/PlaceCard";
import { Link } from "react-router-dom";

export default function HomePage() {
    const regions = ["서울", "경기", "인천", "대전", "전주", "강릉", "경주","서울", "경기", "인천", "대전", "전주", "강릉", "경주"];

    const places = [
        {
            contentId: "1",
            title: "댕댕 포레스트 공원",
            addr1: "서울 성동구 서울숲길 22",
            image:
                "https://images.unsplash.com/photo-1508672019048-805c876b67e2?auto=format&fit=crop&w=1200&q=80",
            overview: "반려견과 함께 바람을 보며 산책하기 좋은 공원",
        },
        {
            contentId: "2",
            title: "카페 하루",
            addr1: "서울 강남구 강남길 21",
            image:
                "https://images.unsplash.com/photo-1516734212186-a967f81ad0d7?auto=format&fit=crop&w=1200&q=80",
            overview: "실내 동반이 가능하고 포토존이 있는 카페",
        },
        {
            contentId: "3",
            title: "한강 수변 산책로",
            addr1: "서울 서구 서동 45",
            image:
                "https://images.unsplash.com/photo-1517849845537-4d257902454a?auto=format&fit=crop&w=1200&q=80",
            overview: "한강 수변길을 반려견과 산책하기 좋은 산책로",
        },
        {
            contentId: "4",
            title: "서울 문화 공원",
            addr1: "서울 용산구 용산로 25",
            image:
                "https://images.unsplash.com/photo-1473773508845-188df298d2d1?auto=format&fit=crop&w=1200&q=80",
            overview: "잔디와 그늘이 잘 되어 있어 쉬어가기 좋은 공원",
        },
        {
            contentId: "5",
            title: "멍멍 해변 산책로",
            addr1: "부산 해운대구 달맞이길 10",
            image:
                "https://images.unsplash.com/photo-1525253013412-55c1a69a5738?auto=format&fit=crop&w=1200&q=80",
            overview: "바다를 보며 여유롭게 걷기 좋은 코스",
        },
        {
            contentId: "6",
            title: "펫 프렌들리 브런치",
            addr1: "경기 수원시 영통구 센트럴로 55",
            image:
                "https://images.unsplash.com/photo-1507146426996-ef05306b995a?auto=format&fit=crop&w=1200&q=80",
            overview: "야외 좌석이 넓고 반려동물 동반이 편한 브런치 카페",
        },
        {
            contentId: "7",
            title: "강릉 솔숲 산책길",
            addr1: "강원 강릉시 솔향로 77",
            image:
                "https://images.unsplash.com/photo-1450778869180-41d0601e046e?auto=format&fit=crop&w=1200&q=80",
            overview: "솔향 가득한 길을 천천히 산책하기 좋은 코스",
        },
        {
            contentId: "8",
            title: "경주 힐링 정원",
            addr1: "경북 경주시 보문로 102",
            image:
                "https://images.unsplash.com/photo-1518717758536-85ae29035b6d?auto=format&fit=crop&w=1200&q=80",
            overview: "가볍게 쉬어가기 좋고 사진 찍기 좋은 정원형 공간",
        },
    ];

    const [keyword, setKeyword] = useState("");
    const [category, setCategory] = useState("");

    return (
        <div>
            <Header />
            <Hero
                keyword={keyword}
                category={category}
                onKeywordChange={setKeyword}
                onCategoryChange={setCategory}
            />

            <main style={{ maxWidth: "1000px", margin: "0 auto", padding: "56px 24px" }}>
                <RegionList regions={regions} />

                <div style={{
                    display: "grid",
                    gridTemplateColumns: "repeat(auto-fit, minmax(220px, 1fr))",
                    gap: "24px",
                }}>
                    {places.map((place) => (
                        <Link key={place.contentId} to={`/place/${place.contentId}`}>
                            <PlaceCard place={place} />
                        </Link>
                    ))}
                </div>
            </main>
        </div>
    );
}