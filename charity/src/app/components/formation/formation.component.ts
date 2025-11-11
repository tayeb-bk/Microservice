import { Component, OnInit } from '@angular/core';
import { Formation, FormationService, FormationToAdd } from '../../services/formation.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-formation',
  templateUrl: './formation.component.html',
  imports: [
    FormsModule,
    CommonModule,
    HttpClientModule
  ],
  styleUrls: ['./formation.component.css']
})
export class FormationComponent implements OnInit {

  formations: Formation[] = [];
  filteredFormations: Formation[] = [];

  // Objet simplifié sans placesRestantes et status
  formationForm = {
    titre: '',
    description: '',
    formateur: '',
    dureeHeures: 0,
    placesMax: 0,
    dateDebut: '',
    dateFin: '',
    type: 'LANGUE',
    lieu: '',
    prerequis: '',
    publicCible: '',
    competencesAcquises: [] as string[],
    associationId: 0
  };

  // Filtre par type
  selectedType: string = 'ALL';

  // Validation des dates
  dateError: string = '';

  isEditing: boolean = false;
  editingId: number | null = null;
  competencesText: string = '';

  // Toast notifications
  showToast: boolean = false;
  toastMessage: string = '';
  toastType: 'success' | 'error' | 'warning' = 'success';
  toastIcon: string = '';

  constructor(private formationService: FormationService) {}

  ngOnInit(): void {
    this.loadFormations();
  }

  loadFormations(): void {
    this.formationService.getAll().subscribe({
      next: data => {
        this.formations = data;
        this.applyFilter();
      },
      // Suppression du console.error
      error: () => this.showError('Erreur lors du chargement des formations')
    });
  }

  // Appliquer le filtre par type
  applyFilter(): void {
    if (this.selectedType === 'ALL') {
      this.filteredFormations = [...this.formations];
    } else {
      this.filteredFormations = this.formations.filter(formation =>
        formation.type === this.selectedType
      );
    }
  }

  // Changer le filtre
  onFilterChange(): void {
    this.applyFilter();
  }

  // Réinitialiser le filtre
  resetFilter(): void {
    this.selectedType = 'ALL';
    this.applyFilter();
  }

  // Validation des dates
  validateDates(): void {
    this.dateError = '';

    if (this.formationForm.dateDebut && this.formationForm.dateFin) {
      const dateDebut = new Date(this.formationForm.dateDebut);
      const dateFin = new Date(this.formationForm.dateFin);

      if (dateFin <= dateDebut) {
        this.dateError = 'La date de fin doit être après la date de début';
      }
    }
  }

  saveFormation(): void {
    // Valider les dates avant soumission
    this.validateDates();
    if (this.dateError) {
      this.showWarning('Veuillez corriger les erreurs de date');
      return;
    }

    if (!this.formationForm.titre || !this.formationForm.type) {
      this.showWarning('Veuillez remplir les champs obligatoires');
      return;
    }

    // Préparer les compétences
    const competencesArray = this.competencesText
      ? this.competencesText.split(',').map(c => c.trim()).filter(c => c !== '')
      : [];

    // 🟢 Mode édition
    if (this.isEditing && this.editingId !== null) {
      // Récupérer la formation originale pour conserver status et placesRestantes
      const originalFormation = this.formations.find(f => f.id === this.editingId);

      const formationToUpdate: Formation = {
        id: this.editingId,
        titre: this.formationForm.titre,
        description: this.formationForm.description,
        formateur: this.formationForm.formateur,
        dureeHeures: this.formationForm.dureeHeures,
        placesMax: this.formationForm.placesMax,
        placesRestantes: originalFormation?.placesRestantes || 0,
        dateDebut: this.formatDateForAPI(this.formationForm.dateDebut),
        dateFin: this.formatDateForAPI(this.formationForm.dateFin),
        status: originalFormation?.status || 'PLANIFIEE',
        type: this.formationForm.type,
        lieu: this.formationForm.lieu,
        prerequis: this.formationForm.prerequis,
        publicCible: this.formationForm.publicCible,
        competencesAcquises: competencesArray,
        associationId: this.formationForm.associationId,
        dateCreation: originalFormation?.dateCreation || new Date().toISOString(),
        dateModification: new Date().toISOString()
      };

      this.formationService.update(this.editingId, formationToUpdate).subscribe({
        next: (updated) => {
          const index = this.formations.findIndex(f => f.id === this.editingId);
          if (index !== -1) {
            this.formations[index] = updated;
          }
          this.applyFilter();
          this.resetForm();
          this.showSuccess('Formation modifiée avec succès!');
        },
        // Suppression du console.error
        error: () => this.showError('Erreur lors de la modification de la formation')
      });

    // 🟢 Mode ajout
    } else {
      const formationToAdd: FormationToAdd = {
        titre: this.formationForm.titre,
        description: this.formationForm.description,
        formateur: this.formationForm.formateur,
        dureeHeures: this.formationForm.dureeHeures,
        placesMax: this.formationForm.placesMax,
        dateDebut: this.formatDateForAPI(this.formationForm.dateDebut),
        dateFin: this.formatDateForAPI(this.formationForm.dateFin),
        type: this.formationForm.type,
        lieu: this.formationForm.lieu,
        prerequis: this.formationForm.prerequis,
        publicCible: this.formationForm.publicCible,
        competencesAcquises: competencesArray,
        associationId: this.formationForm.associationId
      };

      this.formationService.add(formationToAdd).subscribe({
        next: (f) => {
          this.formations.push(f);
          this.applyFilter();
          this.resetForm();
          this.showSuccess('Formation ajoutée avec succès!');
        },
        // Suppression du console.error
        error: () => this.showError('Erreur lors de l\'ajout de la formation')
      });
    }
  }

  editFormation(f: Formation): void {
    this.isEditing = true;
    this.editingId = f.id;

    // Copier les valeurs éditable seulement
    this.formationForm = {
      titre: f.titre || '',
      description: f.description || '',
      formateur: f.formateur || '',
      dureeHeures: f.dureeHeures || 0,
      placesMax: f.placesMax || 0,
      dateDebut: this.formatDateForInput(f.dateDebut),
      dateFin: this.formatDateForInput(f.dateFin),
      type: f.type || 'LANGUE',
      lieu: f.lieu || '',
      prerequis: f.prerequis || '',
      publicCible: f.publicCible || '',
      competencesAcquises: f.competencesAcquises || [],
      associationId: f.associationId || 0
    };

    this.competencesText = f.competencesAcquises?.join(', ') || '';
    this.dateError = ''; // Réinitialiser l'erreur de date
  }

  // Suppression directe sans confirmation
  deleteFormation(id: number): void {
    this.formationService.delete(id).subscribe({
      next: () => {
        this.formations = this.formations.filter(f => f.id !== id);
        this.applyFilter();
        this.showSuccess('Formation supprimée avec succès!');
      },
      // Suppression du console.error
      error: () => this.showError('Erreur lors de la suppression de la formation')
    });
  }

  // Helper methods pour les dates
  private formatDateForAPI(dateString: string): string {
    if (!dateString) return new Date().toISOString();
    return new Date(dateString).toISOString();
  }

  private formatDateForInput(dateString: string): string {
    if (!dateString) return '';
    const date = new Date(dateString);
    // Format pour input date: YYYY-MM-DD
    return date.toISOString().split('T')[0];
  }

  // Gestion des notifications
  showSuccess(message: string): void {
    this.toastMessage = message;
    this.toastType = 'success';
    this.toastIcon = '✅';
    this.showToast = true;
    setTimeout(() => this.hideToast(), 3000);
  }

  showError(message: string): void {
    this.toastMessage = message;
    this.toastType = 'error';
    this.toastIcon = '❌';
    this.showToast = true;
    setTimeout(() => this.hideToast(), 4000);
  }

  showWarning(message: string): void {
    this.toastMessage = message;
    this.toastType = 'warning';
    this.toastIcon = '⚠️';
    this.showToast = true;
    setTimeout(() => this.hideToast(), 4000);
  }

  hideToast(): void {
    this.showToast = false;
  }

  // Obtenir le libellé du type
  getTypeLabel(type: string): string {
    const types: { [key: string]: string } = {
      'LANGUE': 'Langue',
      'COMPETENCE_TECHNIQUE': 'Compétence technique',
      'SENSIBILISATION': 'Sensibilisation',
      'INSERTION_PRO': 'Insertion professionnelle',
      'FORMATION_PROFESSIONNELLE': 'Formation professionnelle',
      'ATELIER_PRATIQUE': 'Atelier pratique',
      'SEMINAIRE': 'Séminaire'
    };
    return types[type] || type;
  }

  resetForm(): void {
    this.formationForm = {
      titre: '',
      description: '',
      formateur: '',
      dureeHeures: 0,
      placesMax: 0,
      dateDebut: '',
      dateFin: '',
      type: 'LANGUE',
      lieu: '',
      prerequis: '',
      publicCible: '',
      competencesAcquises: [],
      associationId: 0
    };
    this.competencesText = '';
    this.dateError = '';
    this.isEditing = false;
    this.editingId = null;
  }

  getTypeClass(type: string): string {
    switch (type) {
      case 'LANGUE': return 'type-langue';
      case 'COMPETENCE_TECHNIQUE': return 'type-competence-technique';
      case 'SENSIBILISATION': return 'type-sensibilisation';
      case 'INSERTION_PRO': return 'type-insertion-pro';
      case 'FORMATION_PROFESSIONNELLE': return 'type-formation-professionnelle';
      case 'ATELIER_PRATIQUE': return 'type-atelier-pratique';
      case 'SEMINAIRE': return 'type-seminaire';
      default: return 'type-default';
    }
  }
}
