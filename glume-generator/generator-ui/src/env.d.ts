interface ImportMetaEnv extends Readonly<Record<string, string>> {
  readonly VITE_APP_NAME: string;
  readonly VITE_APP_ENV: string;
  readonly VITE_PORT: number;
  readonly VITE_HOST: string;
  readonly VITE_BASE_URL: string;
  readonly VITE_APP_BASE_API: string;
  readonly VITE_BUILD_OUT_DIR: string;
  readonly VITE_BUILD_ASSETS_DIR: string;
  readonly VITE_BUILD_SOURCEMAP: boolean;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
