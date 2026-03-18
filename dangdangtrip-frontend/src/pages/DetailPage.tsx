import { useParams } from "react-router-dom";

export default function DetailPage() {
  const { id } = useParams();

  // 임시 더미 데이터 (나중에 API로 교체)
  const place = {
    title: "댕댕 포레스트 공원",
    addr1: "서울 성동구 서울숲길 22",
    image:
      "https://images.unsplash.com/photo-1508672019048-805c876b67e2?auto=format&fit=crop&w=1200&q=80",
    overview:
      "반려견과 함께 바람을 보며 산책하기 좋은 공원입니다. 넓은 잔디와 그늘이 있어 휴식하기 좋습니다.",
    tel: "02-123-4567",
    useTime: "09:00 - 20:00",
    parking: "가능",
  };

  return (
    <div
      style={{
        minHeight: "100vh",
        backgroundColor: "#f6f6f6",
        padding: "80px 24px",
      }}
    >
      <div
        style={{
          maxWidth: "900px",
          margin: "0 auto",
          backgroundColor: "#ffffff",
          borderRadius: "24px",
          overflow: "hidden",
          boxShadow: "0 14px 40px rgba(17, 24, 39, 0.08)",
        }}
      >
        {/* 이미지 */}
        <div
          style={{
            width: "100%",
            height: "360px",
            overflow: "hidden",
          }}
        >
          <img
            src={place.image}
            alt={place.title}
            style={{
              width: "100%",
              height: "100%",
              objectFit: "cover",
            }}
          />
        </div>

        {/* 내용 */}
        <div style={{ padding: "32px" }}>
          <h1
            style={{
              margin: "0 0 12px",
              fontSize: "28px",
              fontWeight: 800,
              color: "#111827",
            }}
          >
            {place.title}
          </h1>

          <p
            style={{
              margin: "0 0 16px",
              color: "#6b7280",
              fontSize: "14px",
            }}
          >
            {place.addr1}
          </p>

          <p
            style={{
              margin: "0 0 24px",
              fontSize: "16px",
              lineHeight: 1.6,
              color: "#374151",
            }}
          >
            {place.overview}
          </p>

          <div
            style={{
              borderTop: "1px solid #e5e7eb",
              paddingTop: "20px",
              display: "grid",
              gap: "12px",
            }}
          >
            <p>
              <strong>전화번호:</strong> {place.tel}
            </p>
            <p>
              <strong>이용시간:</strong> {place.useTime}
            </p>
            <p>
              <strong>주차:</strong> {place.parking}
            </p>
            <p>
              <strong>contentId:</strong> {id}
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
