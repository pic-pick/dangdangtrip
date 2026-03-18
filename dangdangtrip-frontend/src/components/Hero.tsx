import heroBackground from "../assets/hero_background.jpg";
import styles from "./Hero.module.css";
import { useNavigate } from "react-router-dom";

interface HeroProps {
  keyword: string;
  category: string;
  onKeywordChange: (value: string) => void;
  onCategoryChange: (value: string) => void;
}

export default function Hero({
  keyword,
  category,
  onKeywordChange,
  onCategoryChange,
}: HeroProps) {
  const navigate = useNavigate();
  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    const query = new URLSearchParams({
      keyword,
      category,
    }).toString();

    navigate(`/search?${query}`);
  };

  return (
    <section
      className={styles.hero}
      style={{
        backgroundImage: `url(${heroBackground})`,
      }}
    >
      <div className={styles.overlay} />

      <div className={styles.content}>
        <h1 className={styles.title}>반려동물과 함께하는 여행지 탐색</h1>
        <p className={styles.description}>
          특정 지역에서 반려동물 동반 가능한 장소를 찾아보세요.
        </p>

        <form className={styles.searchBox} onSubmit={handleSubmit}>
          <input
            type="text"
            value={keyword}
            onChange={(event) => onKeywordChange(event.target.value)}
            placeholder="여행지 검색"
            className={styles.input}
          />

          <select
            value={category}
            onChange={(event) => onCategoryChange(event.target.value)}
            className={styles.select}
          >
            <option value="all">전체</option>
            <option value="travel">여행지</option>
            <option value="cafe">카페</option>
            <option value="park">공원</option>
          </select>

          <button type="submit" className={styles.button}>
            검색
          </button>
        </form>
      </div>
    </section>
  );
}