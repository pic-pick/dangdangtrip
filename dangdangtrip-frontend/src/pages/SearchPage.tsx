import { useState, useEffect } from "react";
import { useSearchParams, Link } from "react-router-dom";
import { fetchPlaces, type PlaceSummary } from "../api";
import Header from "../components/Header";
import PlaceCard from "../components/PlaceCard";
import styles from "./SearchPage.module.css";
import heroBg from "../assets/hero_background.jpg";

const PAGE_SIZE = 12;

function SkeletonCard() {
    return (
        <div className={styles.skeleton}>
            <div className={styles.skeletonImg} />
            <div className={styles.skeletonLine} />
            <div className={styles.skeletonLineShort} />
        </div>
    );
}

export default function SearchPage() {
    const [searchParams] = useSearchParams();
    const keyword = searchParams.get("keyword") ?? "";

    const [items, setItems] = useState<PlaceSummary[]>([]);
    const [page, setPage] = useState(1);
    const [totalCount, setTotalCount] = useState(0);
    const [loading, setLoading] = useState(false);
    const [initialDone, setInitialDone] = useState(false);

    useEffect(() => {
        setItems([]);
        setPage(1);
        setTotalCount(0);
        setInitialDone(false);
        loadPage(1, true);
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [keyword]);

    const loadPage = async (p: number, replace = false) => {
        setLoading(true);
        try {
            const data = await fetchPlaces({ keyword: keyword || undefined, page: p, size: PAGE_SIZE });
            setItems((prev) => replace ? data.content : [...prev, ...data.content]);
            setTotalCount(data.totalCount);
            setPage(p);
        } finally {
            setLoading(false);
            setInitialDone(true);
        }
    };

    const hasMore = items.length < totalCount;
    const progress = totalCount > 0 ? Math.round((items.length / totalCount) * 100) : 0;

    return (
        <div className={styles.page}>
            <Header />

            {/* 검색 배너 */}
            <div className={styles.searchBanner} style={{ backgroundImage: `url(${heroBg})` }}>
                <div className={styles.searchBannerOverlay} />
                <div className={styles.bannerInner}>
                    <h1 className={styles.searchTitle}>
                        <span className={styles.keyword}>"{keyword}"</span> 검색 결과
                    </h1>
                    {initialDone && (
                        <span className={styles.countBadge}>
                            반려동물 동반 장소 {totalCount.toLocaleString()}곳
                        </span>
                    )}
                </div>
            </div>

            <main className={styles.main}>
                {/* 초기 로딩 — 스켈레톤 */}
                {!initialDone && loading && (
                    <div className={styles.skeletonGrid}>
                        {Array.from({ length: 8 }).map((_, i) => <SkeletonCard key={i} />)}
                    </div>
                )}

                {/* 결과 없음 */}
                {initialDone && items.length === 0 && !loading && (
                    <div className={styles.empty}>
                        <div className={styles.emptyIcon}>🐾</div>
                        <p className={styles.emptyTitle}>검색 결과가 없습니다</p>
                        <p className={styles.emptyDesc}>다른 지역이나 장소 이름으로 다시 검색해보세요.</p>
                    </div>
                )}

                {/* 결과 그리드 */}
                {items.length > 0 && (
                    <div className={styles.grid}>
                        {items.map((place) => (
                            <Link key={place.contentId} to={`/place/${place.contentId}`} style={{ textDecoration: "none" }}>
                                <PlaceCard place={{
                                    contentId: place.contentId,
                                    title: place.title,
                                    addr1: place.addr1,
                                    image: place.firstImage,
                                    overview: place.addr1,
                                }} />
                            </Link>
                        ))}
                    </div>
                )}

                {/* 추가 로딩 */}
                {loading && initialDone && (
                    <p style={{ textAlign: "center", color: "#9ca3af", marginTop: "32px", fontSize: "14px" }}>
                        불러오는 중...
                    </p>
                )}

                {/* 더 보기 */}
                {initialDone && hasMore && !loading && (
                    <div className={styles.loadMore}>
                        <div className={styles.loadMoreBar}>
                            <div className={styles.loadMoreProgress} style={{ width: `${progress}%` }} />
                        </div>
                        <span className={styles.loadMoreCount}>{items.length} / {totalCount}</span>
                        <button className={styles.loadMoreButton} onClick={() => loadPage(page + 1)}>
                            더 보기
                        </button>
                    </div>
                )}
            </main>
        </div>
    );
}
