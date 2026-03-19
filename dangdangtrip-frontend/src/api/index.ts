import axios from 'axios';

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? '',
  headers: {
    'ngrok-skip-browser-warning': 'true',
  },
});

export interface PlaceSummary {
  contentId: string;
  title: string;
  addr1: string;
  addr2: string;
  firstImage: string;
  tel: string;
  areaCode: number | null;
  sigunguCode: number | null;
}

export interface PlacePage {
  page: number;
  size: number;
  totalCount: number;
  content: PlaceSummary[];
}

export interface PlaceDetail {
  contentId: string;
  contentTypeId: string;
  title: string;
  addr1: string;
  addr2: string;
  tel: string;
  homepage: string;
  overview: string;
  firstImage: string;
  images: string[];
  parking: string;
  useTime: string;
  petInfo: string;
  infoCenter: string;
  acmpyTypeCd: string;
  acmpyPsblCpam: string;
  acmpyNeedMtr: string;
  etcAcmpyInfo: string;
  relaPosesFclty: string;
}

export interface Area {
  code: string;
  name: string;
}

export const fetchPlaces = (params?: {
  areaCode?: string;
  sigunguCode?: number;
  contentTypeId?: number;
  keyword?: string;
  page?: number;
  size?: number;
}) => apiClient.get<PlacePage>('/api/places', { params }).then((r) => r.data);

export const fetchPlaceDetail = (contentId: string) =>
  apiClient.get<PlaceDetail>(`/api/places/${contentId}`).then((r) => r.data);

export const fetchAreas = () =>
  apiClient.get<Area[]>('/api/areas').then((r) => r.data);

