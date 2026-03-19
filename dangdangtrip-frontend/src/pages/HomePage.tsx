import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import Header from "../components/Header";
import Hero from "../components/Hero";
import RegionList from "../components/RegionList";
import PlaceCard from "../components/PlaceCard";
import {
    fetchPlaces, fetchAreas,
    type PlaceSummary, type Area,
} from "../api";

const SEOUL_AREA_CODE = "1";
const PAGE_SIZE = 8;

// ── 공공 API 카테고리 ────────────────────────────────────────────
type GovCategoryDef = {
    label: string;
    contentTypeId: number;
};

const GOV_CATEGORIES: GovCategoryDef[] = [
    { label: "관광지",   contentTypeId: 12 },
    { label: "음식점",   contentTypeId: 39 },
    { label: "숙소",     contentTypeId: 32 },
    { label: "문화시설", contentTypeId: 14 },
];

// ── 상태 타입 ────────────────────────────────────────────────────
interface SectionState<T> {
    items: T[];
    page: number;
    hasMore: boolean;
    loading: boolean;
}

function initSection<T>(): SectionState<T> {
    return { items: [], page: 1, hasMore: true, loading: false };
}

// ── 공통 카드 렌더링 ─────────────────────────────────────────────
function PlaceGrid({ children }: { children: React.ReactNode }) {
    return (
        <div style={{
            display: "grid",
            gridTemplateColumns: "repeat(auto-fill, minmax(220px, 1fr))",
            gap: "24px",
        }}>
            {children}
        </div>
    );
}

function LoadMoreButton({ onClick }: { onClick: () => void }) {
    return (
        <div style={{ textAlign: "center", marginTop: "24px" }}>
            <button
                onClick={onClick}
                style={{
                    padding: "10px 32px",
                    borderRadius: "999px",
                    border: "1.5px solid #f7b500",
                    background: "transparent",
                    color: "#f7b500",
                    fontWeight: 600,
                    fontSize: "14px",
                    cursor: "pointer",
                }}
            >
                더 보기
            </button>
        </div>
    );
}

function SectionTitle({ label }: { label: string }) {
    return (
        <h2 style={{ fontSize: "20px", fontWeight: 700, marginBottom: "20px", color: "#111827" }}>
            {label}
        </h2>
    );
}

export default function HomePage() {
    const [regions, setRegions] = useState<Area[]>([]);
    const [selectedAreaCode, setSelectedAreaCode] = useState<string>(SEOUL_AREA_CODE);
    const [keyword, setKeyword] = useState("");

    const [govStates, setGovStates] = useState<Record<number, SectionState<PlaceSummary>>>(() =>
        Object.fromEntries(GOV_CATEGORIES.map((c) => [c.contentTypeId, initSection<PlaceSummary>()]))
    );

    useEffect(() => {
        fetchAreas().then(setRegions).catch(console.error);
    }, []);

    const selectedAreaName = regions.find((r) => r.code === selectedAreaCode)?.name ?? "서울";

    useEffect(() => {
        if (regions.length === 0) return;

        // 공공 API 초기화 + 로드
        setGovStates(Object.fromEntries(GOV_CATEGORIES.map((c) => [c.contentTypeId, initSection<PlaceSummary>()])));
        GOV_CATEGORIES.forEach((cat) => loadGovPage(cat, 1, selectedAreaCode, selectedAreaName, true));

    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [selectedAreaCode, regions]);

    // ── 공공 API 로드 ────────────────────────────────────────────
    const loadGovPage = async (
        cat: GovCategoryDef, page: number, areaCode: string, _areaName: string, replace = false
    ) => {
        setGovStates((prev) => ({ ...prev, [cat.contentTypeId]: { ...prev[cat.contentTypeId], loading: true } }));
        try {
            const params = { areaCode, contentTypeId: cat.contentTypeId, page, size: PAGE_SIZE };
            const data = await fetchPlaces(params);
            setGovStates((prev) => {
                const newItems = replace ? data.content : [...prev[cat.contentTypeId].items, ...data.content];
                return {
                    ...prev,
                    [cat.contentTypeId]: {
                        items: newItems,
                        page,
                        hasMore: newItems.length < data.totalCount && data.content.length === PAGE_SIZE,
                        loading: false,
                    },
                };
            });
        } catch {
            setGovStates((prev) => ({ ...prev, [cat.contentTypeId]: { ...prev[cat.contentTypeId], loading: false } }));
        }
    };

    const handleRegionSelect = (name: string) => {
        const area = regions.find((r) => r.name === name);
        if (!area) return;
        setSelectedAreaCode((prev) => (prev === area.code ? SEOUL_AREA_CODE : area.code));
    };

    return (
        <div>
            <Header />
            <Hero keyword={keyword} onKeywordChange={setKeyword} />

            <main style={{ maxWidth: "1000px", margin: "0 auto", padding: "56px 24px" }}>
                <RegionList
                    regions={regions.map((r) => r.name)}
                    onSelect={handleRegionSelect}
                    selectedRegion={regions.find((r) => r.code === selectedAreaCode)?.name}
                />

                {/* ── 공공 API 섹션 ── */}
                {GOV_CATEGORIES.map((cat) => {
                    const state = govStates[cat.contentTypeId];
                    if (!state || (state.items.length === 0 && !state.loading && !state.hasMore)) return null;
                    return (
                        <section key={cat.contentTypeId} style={{ marginTop: "48px" }}>
                            <SectionTitle label={cat.label} />
                            {state.items.length === 0 && !state.loading ? (
                                <p style={{ color: "#9ca3af", fontSize: "14px" }}>해당 지역에 장소가 없습니다.</p>
                            ) : (
                                <PlaceGrid>
                                    {state.items.map((place) => (
                                        <Link key={place.contentId} to={`/place/${place.contentId}`} style={{ textDecoration: "none" }}>
                                            <PlaceCard place={{ contentId: place.contentId, title: place.title, addr1: place.addr1, image: place.firstImage, overview: place.addr1 }} />
                                        </Link>
                                    ))}
                                </PlaceGrid>
                            )}
                            {state.loading && <p style={{ textAlign: "center", color: "#9ca3af", marginTop: "16px" }}>불러오는 중...</p>}
                            {state.hasMore && !state.loading && (
                                <LoadMoreButton onClick={() => loadGovPage(cat, state.page + 1, selectedAreaCode, selectedAreaName)} />
                            )}
                        </section>
                    );
                })}

            </main>
        </div>
    );
}
