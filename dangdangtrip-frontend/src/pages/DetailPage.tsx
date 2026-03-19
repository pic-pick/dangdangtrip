import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import { fetchPlaceDetail, type PlaceDetail } from "../api";
import Header from "../components/Header";

function PetInfoSection({ place }: { place: PlaceDetail }) {
    const hasPetData = place.acmpyNeedMtr || place.etcAcmpyInfo || place.acmpyPsblCpam || place.relaPosesFclty;
    if (!hasPetData) return null;

    const tags: { label: string; icon: string }[] = [];
    const mtr = place.acmpyNeedMtr ?? "";
    const info = place.etcAcmpyInfo ?? "";

    if (mtr.includes("목줄")) tags.push({ icon: "🦮", label: "목줄 필요" });
    if (mtr.includes("케이지") || mtr.includes("이동장")) tags.push({ icon: "🧳", label: "이동장 필요" });
    if (mtr.includes("자유")) tags.push({ icon: "🐕", label: "자유 이용" });
    if (info.includes("소형")) tags.push({ icon: "🐩", label: "소형견 가능" });
    if (info.includes("중형")) tags.push({ icon: "🐕", label: "중형견 가능" });
    if (info.includes("대형")) tags.push({ icon: "🐕‍🦺", label: "대형견 가능" });

    return (
        <section style={{ marginTop: "48px" }}>
            <h2 style={{ margin: "0 0 20px", fontSize: "18px", fontWeight: 700, color: "#111827" }}>
                🐾 반려동물 동반 정보
            </h2>
            {tags.length > 0 && (
                <div style={{ display: "flex", flexWrap: "wrap", gap: "10px", marginBottom: "20px" }}>
                    {tags.map((t) => (
                        <span key={t.label} style={{
                            display: "inline-flex", alignItems: "center", gap: "6px",
                            padding: "8px 18px", borderRadius: "999px",
                            background: "#f7b500", color: "#fff", fontSize: "14px", fontWeight: 600,
                        }}>
                            {t.icon} {t.label}
                        </span>
                    ))}
                </div>
            )}
            <div style={{
                padding: "20px 24px",
                background: "#fffbeb",
                borderLeft: "4px solid #f7b500",
                borderRadius: "0 8px 8px 0",
                display: "grid", gap: "10px", fontSize: "14px", color: "#374151", lineHeight: 1.6,
            }}>
                {place.acmpyNeedMtr && <p style={{ margin: 0 }}><strong>동반 조건</strong> · {place.acmpyNeedMtr}</p>}
                {place.etcAcmpyInfo && <p style={{ margin: 0 }}><strong>기타 안내</strong> · {place.etcAcmpyInfo}</p>}
                {place.acmpyPsblCpam && <p style={{ margin: 0 }}><strong>동반 가능</strong> · {place.acmpyPsblCpam}</p>}
                {place.relaPosesFclty && <p style={{ margin: 0 }}><strong>보유 시설</strong> · {place.relaPosesFclty}</p>}
            </div>
        </section>
    );
}

function InfoRow({ label, value }: { label: string; value?: string | null }) {
    if (!value) return null;
    return (
        <div style={{ display: "flex", gap: "16px", padding: "16px 0", borderBottom: "1px solid #f3f4f6" }}>
            <span style={{ minWidth: "80px", fontSize: "13px", color: "#9ca3af", fontWeight: 600, paddingTop: "1px" }}>
                {label}
            </span>
            <span style={{ fontSize: "14px", color: "#374151", lineHeight: 1.6 }}>{value}</span>
        </div>
    );
}

export default function DetailPage() {
    const { id } = useParams<{ id: string }>();
    const [place, setPlace] = useState<PlaceDetail | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(false);

    useEffect(() => {
        if (!id) return;
        setLoading(true);
        fetchPlaceDetail(id)
            .then(setPlace)
            .catch(() => setError(true))
            .finally(() => setLoading(false));
    }, [id]);

    if (loading) {
        return (
            <div style={{ minHeight: "100vh", display: "flex", alignItems: "center", justifyContent: "center" }}>
                <p style={{ color: "#9ca3af" }}>불러오는 중...</p>
            </div>
        );
    }

    if (error || !place) {
        return (
            <div style={{ minHeight: "100vh", display: "flex", flexDirection: "column", alignItems: "center", justifyContent: "center", gap: "16px" }}>
                <p style={{ color: "#ef4444" }}>장소 정보를 불러올 수 없습니다.</p>
                <Link to="/" style={{ color: "#f7b500", fontWeight: 600 }}>홈으로 돌아가기</Link>
            </div>
        );
    }

    const heroImage = place.firstImage || place.images?.[0];
    const galleryImages = place.images?.filter((url) => url !== heroImage) ?? [];

    return (
        <div style={{ minHeight: "100vh", backgroundColor: "#fff" }}>
            <Header />

            {/* 히어로 이미지 */}
            {heroImage ? (
                <div style={{ width: "100%", height: "480px", overflow: "hidden", backgroundColor: "#f3f4f6" }}>
                    <img
                        src={heroImage}
                        alt={place.title}
                        style={{ width: "100%", height: "100%", objectFit: "cover" }}
                    />
                </div>
            ) : (
                <div style={{ width: "100%", height: "240px", backgroundColor: "#f3f4f6", display: "flex", alignItems: "center", justifyContent: "center", fontSize: "64px" }}>
                    🐾
                </div>
            )}

            {/* 본문 */}
            <div style={{ maxWidth: "760px", margin: "0 auto", padding: "48px 24px 80px" }}>

                {/* 뒤로가기 */}
                <Link to="/" style={{ fontSize: "13px", color: "#9ca3af", textDecoration: "none", display: "inline-block", marginBottom: "24px" }}>
                    ← 홈으로
                </Link>

                {/* 제목 */}
                <h1 style={{ margin: "0 0 10px", fontSize: "32px", fontWeight: 800, color: "#111827", lineHeight: 1.3 }}>
                    {place.title}
                </h1>
                <p style={{ margin: "0 0 32px", fontSize: "15px", color: "#6b7280" }}>
                    📍 {place.addr1} {place.addr2}
                </p>

                {/* 소개 */}
                {place.overview && (
                    <section>
                        <h2 style={{ margin: "0 0 12px", fontSize: "18px", fontWeight: 700, color: "#111827" }}>장소 소개</h2>
                        <p style={{ margin: 0, fontSize: "15px", lineHeight: 1.8, color: "#374151" }}>
                            {place.overview}
                        </p>
                    </section>
                )}

                {/* 반려동물 동반 정보 */}
                <PetInfoSection place={place} />

                {/* 기본 정보 */}
                {(place.tel || place.useTime || place.parking || place.infoCenter || place.homepage) && (
                    <section style={{ marginTop: "48px" }}>
                        <h2 style={{ margin: "0 0 4px", fontSize: "18px", fontWeight: 700, color: "#111827" }}>기본 정보</h2>
                        <div style={{ borderTop: "2px solid #111827", marginTop: "16px" }}>
                            <InfoRow label="전화번호" value={place.tel} />
                            <InfoRow label="이용시간" value={place.useTime} />
                            <InfoRow label="주차" value={place.parking} />
                            <InfoRow label="문의" value={place.infoCenter} />
                            {place.homepage && (
                                <div style={{ display: "flex", gap: "16px", padding: "16px 0" }}>
                                    <span style={{ minWidth: "80px", fontSize: "13px", color: "#9ca3af", fontWeight: 600 }}>홈페이지</span>
                                    <a href={place.homepage} target="_blank" rel="noreferrer"
                                        style={{ fontSize: "14px", color: "#f7b500", fontWeight: 600, textDecoration: "none" }}>
                                        바로가기 →
                                    </a>
                                </div>
                            )}
                        </div>
                    </section>
                )}

                {/* 추가 사진 */}
                {galleryImages.length > 0 && (
                    <section style={{ marginTop: "48px" }}>
                        <h2 style={{ margin: "0 0 16px", fontSize: "18px", fontWeight: 700, color: "#111827" }}>사진</h2>
                        <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(220px, 1fr))", gap: "8px" }}>
                            {galleryImages.map((url, i) => (
                                <img
                                    key={i}
                                    src={url}
                                    alt={`${place.title} ${i + 1}`}
                                    style={{ width: "100%", aspectRatio: "4/3", objectFit: "cover" }}
                                />
                            ))}
                        </div>
                    </section>
                )}
            </div>
        </div>
    );
}
